package com.springserver.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String role; // Caregiver, Elderly

    private String phoneNumber;

    private String language; // PL, EN

    private String theme;    // LIGHT, DARK

    private String tokenFCM;

    @ManyToMany
    @JoinTable(
            name = "user_relation",
            joinColumns = @JoinColumn(name = "caregiver_id"),
            inverseJoinColumns = @JoinColumn(name = "elderly_id")
    )
    private Set<User> elderly = new HashSet<>();

    @ManyToMany(mappedBy = "elderly")
    private Set<User> caregiver = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Incident> reports = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_incident",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "incident_id")
    )
    private Set<Incident> incidents = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getTokenFCM() { return tokenFCM; }
    public void setTokenFCM(String tokenFCM) { this.tokenFCM = tokenFCM; }

    public Set<User> getElderly() {
        return elderly;
    }
    public void setElderly(Set<User> dependents) {
        this.elderly = dependents;
    }
    public void addElderly(User user) {
        this.elderly.add(user);
    }
    public void removeElderly(User user) {
        this.elderly.remove(user);
    }

    public Set<User> getCaregiver() {
        return caregiver;
    }
    public void setCaregiver(Set<User> guardians) {
        this.caregiver = guardians;
    }
    public void addCaregiver(User user) {
        this.caregiver.add(user);
    }
    public void removeCaregiver(User user) {
        this.caregiver.remove(user);
    }

    public Set<Incident> getReports() {
        return reports;
    }
    public void setReports(Set<Incident> reports) {
        this.reports = reports;
    }
    public void addReport(Incident report) {
        reports.add(report);
        report.setUser(this);
    }
    public void removeReport(Incident report) {
        reports.remove(report);
        report.setUser(null);
    }

    public Set<Incident> getIncidents() {
        return incidents;
    }
    public void setIncidents(Set<Incident> incidents) {
        this.incidents = incidents;
    }
    public void addIncident(Incident incident) {
        incidents.add(incident);
    }
    public void removeIncident(Incident incident) {
        incidents.remove(incident);
    }
}
