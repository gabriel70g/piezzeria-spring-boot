package com.gabys.pizza.persistence.audit;

import com.gabys.pizza.persistence.entity.PizzaEntity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;

public class AuditPizzaListener {

    private PizzaEntity currentValue;

    @PostLoad
    public void postLoad(PizzaEntity entity){
        System.out.println("POST LOAD:");
        this.currentValue = entity;

    }

    @PostPersist
    @PostUpdate
    public void onPostPersist(PizzaEntity pizzaEntity){
        System.out.println("POST PERSIST OR UPDATE");
        System.out.println("OLD VALUE: " + this.currentValue);
        System.out.println("NEW VALUE: "+ pizzaEntity.toString());
    }

    @PreRemove
    public void onPreDelete(PizzaEntity pizzaEntity){
        System.out.println(pizzaEntity.toString());
    }
}
