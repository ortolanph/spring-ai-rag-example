package pt.pauloortolan.springairagexample.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.pauloortolan.springairagexample.services.SimpleAIService;

@Slf4j
@RestController
@RequestMapping("/simple")
@RequiredArgsConstructor
public class SimpleAIController {

    private final SimpleAIService simpleAIService;

    @GetMapping("/meaningOfLife/{language}")
    public String getMeaningOfLife(@PathVariable String language) {
        return simpleAIService.sayMeaningOfLifeIn(language);
    }

}
