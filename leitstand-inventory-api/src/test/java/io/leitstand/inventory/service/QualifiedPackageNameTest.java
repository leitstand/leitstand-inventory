package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.QualifiedPackageName.newQualifiedPackageName;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class QualifiedPackageNameTest {

    @Test
    public void create_qualified_package_name() {

        QualifiedPackageName qn = newQualifiedPackageName()
                                  .withOrganization("io.leitstand")
                                  .withName("test")
                                  .build();
        
        assertThat(qn.getOrganization(),is("io.leitstand"));
        assertThat(qn.getName(),is("test"));
        assertThat(qn.toString(),is("io.leitstand.test"));
    }

    
    @Test
    public void order_by_package_name() {

        QualifiedPackageName aaa = newQualifiedPackageName()
                                   .withOrganization("io.leitstand")
                                   .withName("aaa")
                                   .build();

        QualifiedPackageName bbb = newQualifiedPackageName()
                                   .withOrganization("io.leitstand")
                                   .withName("bbb")
                                   .build();
        
        QualifiedPackageName ccc = newQualifiedPackageName()
                                   .withOrganization("io.leitstand")
                                   .withName("ccc")
                                   .build();

        
        SortedSet<QualifiedPackageName> packages = new TreeSet<>(asList(aaa,bbb,ccc));
        
        Iterator<QualifiedPackageName> iter = packages.iterator();
        
        assertThat(iter.next().getName(),is("aaa"));
        assertThat(iter.next().getName(),is("bbb"));        
        assertThat(iter.next().getName(),is("ccc"));        
        assertThat(iter.hasNext(),is(false));

    }
    
    
    @Test
    public void order_by_organization() {

        QualifiedPackageName aaa = newQualifiedPackageName()
                                   .withOrganization("aaa")
                                   .withName("ZZZ")
                                   .build();

        QualifiedPackageName bbb = newQualifiedPackageName()
                                   .withOrganization("bbb")
                                   .withName("YYY")
                                   .build();
        
        QualifiedPackageName ccc = newQualifiedPackageName()
                                   .withOrganization("ccc")
                                   .withName("XXX")
                                   .build();

        
        SortedSet<QualifiedPackageName> packages = new TreeSet<>(asList(aaa,bbb,ccc));
        
        Iterator<QualifiedPackageName> iter = packages.iterator();
        
        assertThat(iter.next().getName(),is("ZZZ"));
        assertThat(iter.next().getName(),is("YYY"));        
        assertThat(iter.next().getName(),is("XXX"));        
        assertThat(iter.hasNext(),is(false));

    }

    
}
