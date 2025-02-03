package br.com.spg.bancodigital.dtos;

import br.com.spg.bancodigital.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
}
