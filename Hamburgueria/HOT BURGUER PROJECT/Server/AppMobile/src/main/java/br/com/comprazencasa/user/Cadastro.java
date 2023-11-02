/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.user;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class Cadastro implements Serializable {

    private int id ;
    private String img_path, nome, cpf, email, senha, terms_and_policy ;
    private final Contato contato ;
    private final Endereco endereco ;

    public Cadastro(){
        this.contato = new Contato();
        this.endereco = new Endereco();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Contato getContato() {
        return contato;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getTerms_and_policy() {
        return terms_and_policy;
    }

    public void setTerms_and_policy(String terms_and_policy) {
        this.terms_and_policy = terms_and_policy;
    }
}