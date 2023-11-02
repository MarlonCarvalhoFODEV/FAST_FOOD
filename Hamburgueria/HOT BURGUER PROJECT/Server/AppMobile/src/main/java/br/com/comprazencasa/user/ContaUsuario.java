/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Marlon
 */
public class ContaUsuario implements Serializable {
    private int id, idCustomer, result;
    private String pathImg, name, email, cpf, date, dateOfBirth, password ;
    private char sexy ;
    private boolean termsOfUseAndPolicyPrivacity, banned ;
    private Endereco endereco ;
    private Contato contato ;

    private Set<Integer> favorites ;

    public ContaUsuario(){
        this.favorites = new HashSet<>();
        this.contato = new Contato();
        this.endereco = new Endereco();
    }

    public ContaUsuario(int idUser){
        this.id = idUser ;
        this.favorites = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dateOfBirth;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dateOfBirth = dataNascimento;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenha() {
        return password;
    }

    public void setSenha(String senha) {
        this.password = senha;
    }

    public boolean isTermsOfUseAndPolicyPrivacity() {
        return termsOfUseAndPolicyPrivacity;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setTermsOfUseAndPolicyPrivacity(boolean termsOfUseAndPolicyPrivacity) {
        this.termsOfUseAndPolicyPrivacity = termsOfUseAndPolicyPrivacity;
    }

    public char getSexy() {
        return sexy;
    }

    public void setSexy(char sexy) {
        this.sexy = sexy;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Set<Integer> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Integer> favorites) {
        this.favorites = favorites;
    }
}