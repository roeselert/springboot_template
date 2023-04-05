package org.springframework.samples.petclinic;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.Formatters.joinSingleQuoted;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameContaining;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "org.springframework.samples.petclinic")
public class MyArchitectureTest {

    @ArchTest
    public static final ArchRule myRuleCycles = slices()
            .matching("org.springframework.samples.petclinic.(*)..")
            .should().beFreeOfCycles();

    /*
    @ArchTest
    public static final ArchRule myLayer = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..persistence..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");

     */

    @ArchTest
    public static final ArchRule myLayer = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy(simpleNameEndingWith("Controller").or(simpleNameEndingWith("Formatter")))
            .layer("Persistence").definedBy(simpleNameEndingWith("Repository"))
            .layer("Test").definedBy(simpleNameContaining("Test"))

            .whereLayer("Controller").mayOnlyBeAccessedByLayers("Test")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Controller","Test");

}
