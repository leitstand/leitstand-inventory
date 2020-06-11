package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ReleaseId.releaseId;
import static javax.persistence.EnumType.STRING;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.ReleaseNameConverter;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseState;

@Entity
@Table(schema="inventory", name="release")
@NamedQuery(name="Release.findById",
            query="SELECT r FROM Release r WHERE r.uuid=:uuid")
@NamedQuery(name="Release.findByName",
            query="SELECT r FROM Release r WHERE r.name=:name")
@NamedQuery(name="Release.findByNamePattern",
            query="SELECT r FROM Release r WHERE CAST(r.name AS TEXT) REGEXP :name ORDER BY r.name")    
public class Release extends VersionableEntity {

    private static final long serialVersionUID = 1L;

    public static Query<Release> findReleaseById(ReleaseId id){
        return em -> em.createNamedQuery("Release.findById",Release.class)
                       .setParameter("uuid",id.toString())
                       .getSingleResult();
    }
    
    public static Query<Release> findReleaseByName(ReleaseName name){
        return em -> em.createNamedQuery("Release.findByName",Release.class)
                       .setParameter("name",name)
                       .getSingleResult();
    }
    
    public static Query<List<Release>> findReleaseByName(String name){
        return em -> em.createNamedQuery("Release.findByNamePattern",Release.class)
                       .setParameter("name",name)
                       .getResultList();
    }
    
    @Convert(converter=ReleaseNameConverter.class)
    private ReleaseName name;
    @Enumerated(STRING)
    private ReleaseState state;
    private String description;
    
    @ManyToMany
    @JoinTable(schema="INVENTORY",
                   name="release_image", 
                   joinColumns= @JoinColumn(name="image_id", referencedColumnName="id"),
                   inverseJoinColumns=@JoinColumn(name="release_id",referencedColumnName="id"))
    private List<Image> images;
    
    
    protected Release() {
        
    }
    
    public Release(ReleaseId releaseId,
                   ReleaseName releaseName) {
        super(releaseId.toString());
        this.name = releaseName;
    }
    
    public ReleaseName getReleaseName() {
        return name;
    }
    
    public void setReleaseName(ReleaseName name) {
        this.name = name;
    }

    public ReleaseState getState() {
        return state;
    }
    
    public void setState(ReleaseState state) {
        this.state = state;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public List<Image> getImages() {
        return images;
    }
    
    public void setImages(List<Image> images) {
        this.images = images;
    }

    public ReleaseId getReleaseId() {
        return releaseId(getUuid());
    }
    
}
