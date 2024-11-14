package com.example.manyToOne.projection;

import org.springframework.beans.factory.annotation.Value;

public interface UserProjection {

    String getFirstName();

    String getLastName();

    String getEmail();

    Boolean getStatus();

    String getPassword();

    Boolean getDeactivate();

    String getOrganizationName();

    String getAddress();

}
