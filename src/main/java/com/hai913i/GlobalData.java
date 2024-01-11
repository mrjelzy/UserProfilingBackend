package com.hai913i;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // Cette annotation est optionnelle car singleton est la portée par défaut
public class GlobalData {
    private Long actualUserId;

    // Getters et setters
    public Long getActualUserId() {
        return actualUserId;
    }

    public void setActualUserId(Long actualUserId) {
        this.actualUserId = actualUserId;
    }
}