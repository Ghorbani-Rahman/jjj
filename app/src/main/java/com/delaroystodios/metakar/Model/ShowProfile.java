package com.delaroystodios.metakar.Model;



public class ShowProfile
{
    private int id;
    private String leader_id;
    private String name;
    private String family;
    private String status;
    private String username;
    private String email;
    private String created_at;
    private String updated_at;
    private InformationUser metakar_user_personal_information;
    private String unread_notifications_count;
    private String subset_count;

    public String getSubset_count() {
        return subset_count;
    }

    public void setSubset_count(String subset_count) {
        this.subset_count = subset_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(String leader_id) {
        this.leader_id = leader_id;
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

    public InformationUser getMetakar_user_personal_information() {
        return metakar_user_personal_information;
    }

    public String getUnread_notifications_count() {
        return unread_notifications_count;
    }

    public void setUnread_notifications_count(String unread_notifications_count) {
        this.unread_notifications_count = unread_notifications_count;
    }

    public void setMetakar_user_personal_information(InformationUser metakar_user_personal_information) {
        this.metakar_user_personal_information = metakar_user_personal_information;
    }
}
