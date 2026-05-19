package com.jobsearch.security.converter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@Component
public class SupabaseJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final String EMAIL = "email";

  private static final String APP_METADATA = "app_metadata";

  private static final String ROLES = "roles";

  private static final String ROLE_PREFIX = "ROLE_";

  private static final String DEFAULT_ROLE = "ROLE_USER";

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    String principal = jwt.getClaimAsString(EMAIL);
    if (!hasText(principal)) {
      principal = jwt.getSubject();
    }

    Collection<GrantedAuthority> authorities = extractRoles(jwt);
    return new JwtAuthenticationToken(jwt, authorities, principal);
  }

  private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
    Map<String, Object> appMetadata = jwt.getClaimAsMap(APP_METADATA);
    if (isNull(appMetadata)
        || !appMetadata.containsKey(ROLES)
        || !(appMetadata.get(ROLES) instanceof List<?>)) {
      return List.of(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    List<String> roleList = (List<String>) appMetadata.get(ROLES);
    if (isEmpty(roleList)) {
      return List.of(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    return roleList.stream()
                   .filter(StringUtils::hasText)
                   .map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r.toUpperCase()))
                   .collect(Collectors.toList());
  }
}
