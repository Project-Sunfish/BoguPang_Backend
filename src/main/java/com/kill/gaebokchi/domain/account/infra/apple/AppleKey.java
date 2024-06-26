package com.kill.gaebokchi.domain.account.infra.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class AppleKey {
    private List<Key> keys;
    @Getter
    @Setter
    public static class Key{
            String kty;
            String kid;
            String use;
            String alg;
            String n;
            String e;
    }
    public Optional<AppleKey.Key> getMatchedKeyBy(String kid, String alg){
        return this.keys.stream()
                .filter(key->key.getKid().equals(kid)&&key.getAlg().equals(alg))
                .findFirst();
    }
}