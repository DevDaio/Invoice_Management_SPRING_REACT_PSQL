package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;

// TODO: SecurityConfig mit @Configuration + @EnableWebSecurity
// - SecurityFilterChain Bean:
//   - CSRF ausschalten
//   - SessionCreationPolicy.STATELESS
//   - /login für alle freigeben (permitAll)
//   - alle anderen Endpoints → authenticated
//   - JwtAuthFilter vor UsernamePasswordAuthenticationFilter einhängen
// - PasswordEncoder Bean (BCryptPasswordEncoder)
