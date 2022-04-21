package com.metasis.ikamet;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.metasis.ikamet");

        noClasses()
            .that()
            .resideInAnyPackage("com.metasis.ikamet.service..")
            .or()
            .resideInAnyPackage("com.metasis.ikamet.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.metasis.ikamet.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
