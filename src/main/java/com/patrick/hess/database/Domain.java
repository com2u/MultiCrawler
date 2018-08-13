package com.patrick.hess.database;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.Instant;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "domain",
        uniqueConstraints = {
                @UniqueConstraint(name = "uniqueSubdomain", columnNames = "subdomain")
        })
@GenericGenerator(
        name = "seq_domain_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = @org.hibernate.annotations.Parameter(
                name = "sequence_name", value = "seq_domain_id")
)
public class Domain {

    @Id
    @GeneratedValue(generator = "seq_domain_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "domain", length = 255, nullable = false, updatable = false)
    private String domain;

    @Column(name = "subdomain", length = 255, nullable = false, updatable = false)
    private String subdomain;

    @Column(name = "ip", length = 255, nullable = false, updatable = false)
    private String ip;

    @Column(name = "created_date", nullable = false, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant createdDate;


    public Long getId() {
        return id;
    }

    public Domain setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public Domain setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public Domain setSubdomain(String subdomain) {
        this.subdomain = subdomain;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Domain setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Domain setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain1 = (Domain) o;
        return Objects.equals(id, domain1.id) &&
                Objects.equals(domain, domain1.domain) &&
                Objects.equals(subdomain, domain1.subdomain) &&
                Objects.equals(ip, domain1.ip) &&
                Objects.equals(createdDate, domain1.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domain, subdomain, ip, createdDate);
    }

    @Override
    public String toString() {
        return "Domain{" +
                "id=" + id +
                ", domain='" + domain + '\'' +
                ", subdomain='" + subdomain + '\'' +
                ", ip='" + ip + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}

