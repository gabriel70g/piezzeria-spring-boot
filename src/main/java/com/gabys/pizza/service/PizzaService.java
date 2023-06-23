package com.gabys.pizza.service;

import com.gabys.pizza.persistence.entity.PizzaEntity;
import com.gabys.pizza.persistence.repository.PizzaPagSortRepository;
import com.gabys.pizza.persistence.repository.PizzaRepository;
import com.gabys.pizza.service.dto.UpdatePizzaPriceDto;
import com.gabys.pizza.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;


    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }


    public Page<PizzaEntity> getAll(int page, int elements) {

        PageRequest pageRequest = PageRequest.of(page, elements);

        return pizzaPagSortRepository.findAll(pageRequest);

    }

    public Page<PizzaEntity> getAvailable(int page, int elements, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        PageRequest pageRequest = PageRequest.of( page, elements, sort ) ;

        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
    }

    public PizzaEntity get(int idPizza){
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity save( PizzaEntity pizzaEntity){
        return pizzaRepository.save(pizzaEntity);
    }

    public boolean exists(int idPizza){
        return pizzaRepository.existsById(idPizza);
    }

    public void delete(int idPizza){
        pizzaRepository.deleteById(idPizza);
    }

    public PizzaEntity getByName(String name){
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name)
                .orElseThrow( () -> new RuntimeException("La Pizza no existe"))
                ;
    }

    public List<PizzaEntity> getWith(String ingredient ){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getWithout(String ingredient ){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getCheapest(double price){
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    @Transactional(noRollbackFor = EmailApiException.class)
    public void updatePrice(UpdatePizzaPriceDto dto){
        this.pizzaRepository.updatePrice(dto);
        sendEmail();
    }

    private void sendEmail(){
        throw new EmailApiException();
    }

}
