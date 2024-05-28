package com.kill.gaebokchi.domain.bogu.service;import com.kill.gaebokchi.domain.bogu.entity.Category;import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;import com.kill.gaebokchi.domain.bogu.entity.Type;import com.kill.gaebokchi.domain.bogu.entity.dto.EvolutionRequestDTO;import com.kill.gaebokchi.domain.bogu.entity.dto.EvolvedBoguResponseDTO;import com.kill.gaebokchi.domain.bogu.entity.dto.PopBoguResponseDTO;import com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu.CollectedBoguResponseDTO;import com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu.DogamBoguResponseDTO;import com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu.LiberatedBoguResponseDTO;import com.kill.gaebokchi.domain.bogu.repository.DefaultBoguRepository;import com.kill.gaebokchi.domain.bogu.repository.EvolvedBoguRepository;import com.kill.gaebokchi.domain.user.entity.Member;import com.kill.gaebokchi.domain.user.repository.MemberRepository;import com.kill.gaebokchi.global.error.BadRequestException;import com.kill.gaebokchi.global.error.ExceptionCode;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.cglib.core.Local;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.time.Duration;import java.time.LocalDateTime;import java.util.*;import java.util.stream.Collectors;import static com.kill.gaebokchi.global.error.ExceptionCode.*;@Service@Slf4j@Transactional(readOnly = true)@RequiredArgsConstructorpublic class EvolvedBoguService {    private final EvolvedBoguRepository evolvedBoguRepository;    private final DefaultBoguRepository defaultBoguRepository;    private final DefaultBoguService defaultBoguService;    public EvolvedBoguResponseDTO findEvolvedBoguByID(Member member, Long id) {        EvolvedBogu evolvedBogu = evolvedBoguRepository.findByHostAndId(member, id)                .orElseThrow(() -> new BadRequestException(NOT_FOUND_EVOLVED_BOGU_ID));        return EvolvedBoguResponseDTO.from(evolvedBogu);    }    @Transactional    public List<EvolvedBoguResponseDTO> findAndUpdateEvolvedBoguByHost(Member member){        List<EvolvedBogu> findList = evolvedBoguRepository.findByHostAndIsLiberatedFalse(member);        for(EvolvedBogu findOne : findList){            Integer findCount = findOne.getCount();            Integer findLevel = findOne.getLevel();            LocalDateTime findCreatedAt = findOne.getCreatedAt();            LocalDateTime findBangAt = findOne.getLastBangAt();            Integer newStatus = getStatus(findLevel, findCreatedAt);            findOne.setStatus(newStatus);            if(findBangAt!=null){                Integer newCount = getCount(findCount, findLevel, findBangAt);                findOne.setCount(newCount);            }        }        return findList.stream()                .map(EvolvedBoguResponseDTO::from)                .collect(Collectors.toList());    }    @Transactional    public EvolvedBoguResponseDTO saveEvolvedBogu(Member member, EvolutionRequestDTO evolutionRequestDTO){        if(evolutionRequestDTO.hasNullFields()){            throw new BadRequestException(INVALID_EVOLUTION_FORM);        }        List<Category> categories;        categories = evolutionRequestDTO.getCategories().stream()                .map(Category::fromText)                .collect(Collectors.toList());        if(categories.size()>3){            throw new BadRequestException(EXCEED_CATEGORY_COUNT);        }        EvolvedBogu entity = new EvolvedBogu();        entity.setProblem(evolutionRequestDTO.getProblem());        entity.setCategories(categories);        Collections.shuffle(categories);        Category selectedCategory = categories.get(0);        entity.setSelectedCategory(selectedCategory);        Random rand = new Random();        Integer variation = rand.nextInt(getVariation(selectedCategory));        entity.setVariation(variation);        entity.setName(Type.getNameFromCategoryAndVariation(selectedCategory.getId(), variation));        entity.setLevel(getLevel(member, evolutionRequestDTO.getDefaultBoguId(), selectedCategory));        entity.setStatus(1);        entity.setCount(1);        entity.setCountBang(0);        entity.setLastBangAt(null);        entity.setIsLiberated(false);        entity.setLiberatedAt(null);        DefaultBogu defaultForm = defaultBoguService.findDefaultBoguByID(member, evolutionRequestDTO.getDefaultBoguId());        if(defaultForm.getEvolvedForm()!=null){            throw new BadRequestException(DUPLICATE_EVOLUTION);        }        defaultForm.addEvolvedForm(entity);        defaultBoguRepository.save(defaultForm);        evolvedBoguRepository.save(entity);        return EvolvedBoguResponseDTO.from(entity);    }    @Transactional    public PopBoguResponseDTO popEvolvedBogu(Member member, Long id){        EvolvedBogu findOne = evolvedBoguRepository.findByHostAndId(member, id)                .orElseThrow(() -> new BadRequestException(NOT_FOUND_EVOLVED_BOGU_ID));        if(findOne.getIsLiberated()){            throw new BadRequestException(POP_TO_ALREADY_LIBERATION);        }        Integer findCount = findOne.getCount();        findOne.setCountBang(findOne.getCountBang()+1);        findOne.setLastBangAt(LocalDateTime.now());        if(findCount<=0){            throw new BadRequestException(NEGATIVE_EVOLVED_BOGU_COUNT);        }else{            findOne.setCount(findCount-1);        }        PopBoguResponseDTO res = new PopBoguResponseDTO();        res.setLiberationFlag((findOne.getCountBang()%3)==0);        return res;    }    @Transactional    public void LiberateEvolvedBogu(Member member, Long id){        EvolvedBogu findOne = evolvedBoguRepository.findByHostAndId(member, id)                .orElseThrow(() -> new BadRequestException(NOT_FOUND_EVOLVED_BOGU_ID));        if(findOne.getIsLiberated()){            throw new BadRequestException(DUPLICATE_LIBERATION);        }else{            findOne.setIsLiberated(true);            findOne.setLiberatedAt(LocalDateTime.now());        }    }    private Integer getLevel(Member member, Long id, Category selectedCategory){        DefaultBogu defaultBogu = defaultBoguService.findDefaultBoguByID(member, id);        Member host = defaultBogu.getHost();        Integer countBogus = defaultBoguRepository.findByHostAndEvolvedFormNotNull(host).size();        if(countBogus<=10){            if(countBogus==0) return 1;            Integer n = evolvedBoguRepository.findByHostAndSelectedCategory(host, selectedCategory).size();            Integer x = n/countBogus*100;            if(0<=x && x<=30) return 1;            else if(x<=50) return 2;            else return 3;        }else{            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);            Integer n = evolvedBoguRepository.findByHostAndSelectedCategoryAndCreatedAtAfter(host, selectedCategory, thirtyDaysAgo).size();            Integer m = evolvedBoguRepository.findByHostAndCreatedAtAfter(host, thirtyDaysAgo).size();            if(m==0) return 1;            Integer x = n/m*100;            if(0<=x && x<=10) return 1;            else if(x<=20) return 2;            else if(x<=30) return 3;            else if(x<=40) return 4;            else if(x<=50) return 5;            else return 6;        }    }    private Integer getVariation(Category category){        if(category==Category.CATEGORY2 || category==Category.CATEGORY6) return 3;        else if(category==Category.CATEGORY7) return 1;        else if(category==Category.CATEGORY3 || category==Category.CATEGORY4) return 4;        else return 2;    }    private Integer getStatus(Integer level, LocalDateTime createdAt){        LocalDateTime now = LocalDateTime.now();        Duration duration = Duration.between(createdAt, now);        Integer speed;        switch (level) {            case 1: speed = 6; break;            case 2: speed = 5; break;            case 3: speed = 4; break;            case 4: speed = 3; break;            case 5: speed = 2; break;            case 6: speed = 1; break;            default: throw new BadRequestException(INVALID_LEVEL);        }        if(duration.toHours() >= 8 * speed){            return 9;        }else if(duration.toHours() >= 7 * speed){            return 8;        }else if(duration.toHours() >= 6 * speed){            return 7;        }else if(duration.toHours() >= 5 * speed){            return 6;        }else if(duration.toHours() >= 4 * speed){            return 5;        }else if(duration.toHours() >= 3 * speed){            return 4;        }else if(duration.toHours() >= 2 * speed){            return 3;        }else if(duration.toHours() >= 1 * speed){            return 2;        }else{            return 1;        }    }    private Integer getCount(Integer count, Integer level, LocalDateTime LastBangAt){        LocalDateTime now = LocalDateTime.now();        Duration duration = Duration.between(LastBangAt, now);        Integer maxBangs;        Integer bangFrequency;        switch (level) {            case 1: maxBangs = 3; bangFrequency = 6; break;            case 2: maxBangs = 4; bangFrequency = 5; break;            case 3: maxBangs = 5; bangFrequency = 4; break;            case 4: maxBangs = 5; bangFrequency = 3; break;            case 5: maxBangs = 6; bangFrequency = 2; break;            case 6: maxBangs = 7; bangFrequency = 1; break;            default: throw new BadRequestException(INVALID_LEVEL);        }        if(duration.toHours() >=bangFrequency && count<=maxBangs){            return count+1;        }else{            return count;        }    }    /*for liberated bogus*/    public DogamBoguResponseDTO getDogamBogus(Member member){        List<EvolvedBogu> liberatedBogus = evolvedBoguRepository.findByHostAndIsLiberatedTrue(member);        List<CollectedBoguResponseDTO> collectedBogus = groupEvolvedBogus(liberatedBogus);        DogamBoguResponseDTO res = new DogamBoguResponseDTO();        res.setCountKinds(collectedBogus.size());        res.setCollectedBogus(collectedBogus);        return res;    }    public List<CollectedBoguResponseDTO> groupEvolvedBogus(List<EvolvedBogu> liberatedBogus){        Map<Category, Map<Integer, List<EvolvedBogu>>> bogusGroupedByCategory = liberatedBogus.stream()                .collect(Collectors.groupingBy(                        bogu->bogu.getSelectedCategory(),                        Collectors.groupingBy(bogu->bogu.getVariation())                ));        List<CollectedBoguResponseDTO> collectedBogus = new ArrayList<>();        for(Map.Entry<Category, Map<Integer, List<EvolvedBogu>>> categoryEntry : bogusGroupedByCategory.entrySet()){            Category category = categoryEntry.getKey();            Map<Integer, List<EvolvedBogu>> bogusGroupedByVariation = categoryEntry.getValue();            for(Map.Entry<Integer, List<EvolvedBogu>> variationEntry : bogusGroupedByVariation.entrySet()){                Integer variation = variationEntry.getKey();                List<EvolvedBogu> groupedBogus = variationEntry.getValue();                List<LiberatedBoguResponseDTO> groupedBogusDTO = groupedBogus.stream()                        .map(LiberatedBoguResponseDTO::from)                        .collect(Collectors.toList());                CollectedBoguResponseDTO collectedBogu = new CollectedBoguResponseDTO();                collectedBogu.setId(Type.getIdFromCategoryAndVariation(category.getId(), variation));                collectedBogu.setSelectedCategory(category.getText());                collectedBogu.setVariation(variation);                collectedBogu.setCount(groupedBogusDTO.size());                collectedBogu.setLiberatedBogus(groupedBogusDTO);                collectedBogus.add(collectedBogu);            }        }        return collectedBogus;    }    public CollectedBoguResponseDTO findDogamBoguById(Member member, Integer id){        Type type = Type.fromId(id);        Category category = Category.fromId(type.getCategory());        Integer variation = type.getVariation();        List<LiberatedBoguResponseDTO> liberatedBogus = evolvedBoguRepository.findByHostAndType(member, category, variation).stream()                .map(LiberatedBoguResponseDTO::from)                .collect(Collectors.toList());        CollectedBoguResponseDTO res = new CollectedBoguResponseDTO();        res.setLiberatedBogus(liberatedBogus);        res.setCount(liberatedBogus.size());        res.setVariation(variation);        res.setSelectedCategory(category.getText());        res.setId(id);        return res;    }}