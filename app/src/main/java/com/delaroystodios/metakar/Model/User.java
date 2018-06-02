package com.delaroystodios.metakar.Model;


public class User
{
    private String id;
    private String name;
    private String family;
    private String leader;
    private String status;
    private String username;
    private String email;
    private String created_at;
    private String updated_at;
    private String leader_count;
    private String subset_count;
    private String rules_confirmed;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLeader_count() {
        return leader_count;
    }

    public void setLeader_count(String leader_count) {
        this.leader_count = leader_count;
    }

    public String getSubset_count() {
        return subset_count;
    }

    public void setSubset_count(String subset_count) {
        this.subset_count = subset_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getRules_confirmed() {
        return rules_confirmed;
    }

    public void setRules_confirmed(String rules_confirmed) {
        this.rules_confirmed = rules_confirmed;
    }
}
