import org.apache.commons.lang3.RandomStringUtils

grails.databinding.convertEmptyStringsToNull = false

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'smspanel.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'smspanel.UserRole'
grails.plugin.springsecurity.authority.className = 'smspanel.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/index.html', access: ['permitAll']],
        [pattern: '/static/**/**', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/api/guest/**', filters: 'anonymousAuthenticationFilter,restTokenValidationFilter,restExceptionTranslationFilter,filterInvocationInterceptor'],
        [pattern: '/api/**/**', filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],
        [pattern: '/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter']
]

grails.plugin.springsecurity.rest.token.storage.jwt.expiration = 365*24*3600 // seconds
grails.plugin.springsecurity.rest.token.storage.jwt.secret = RandomStringUtils.randomAlphanumeric(32)
