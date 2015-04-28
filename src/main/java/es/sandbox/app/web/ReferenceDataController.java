package es.sandbox.app.web;

import es.sandbox.app.web.control.Option;
import es.sandbox.app.web.control.Selector;
import es.sandbox.app.web.control.Transformer;
import es.sandbox.app.web.control.Transformer.NopTransformer;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jeslopalo on 24/04/15.
 */
@RestController
@RequestMapping("/data")
public class ReferenceDataController {

    private final Transformer transformer;

    public ReferenceDataController() {
        this.transformer= new NopTransformer();
    }

    public ReferenceDataController(final Transformer transformer) {
        this.transformer= transformer;
    }

    @RequestMapping(value = "/first-values", method = RequestMethod.GET)
    public Selector getFirstValues(@RequestParam(value = "path", required = true) final String path) {
        final Selector selector = new Selector(path, "http://localhost:8080/data/second-values?path=" + path + "&firstValue=%s");

        selector.add(new Option("Choose one...", null));
        selector.add(new Option("value 1", 1));
        selector.add(new Option("value 2", 2));
        selector.add(new Option("value 3", 3));

        return selector;
    }

    @RequestMapping(value = "/second-values", method = RequestMethod.GET)
    public Selector getSecondValues(@RequestParam(value = "path", required = true) final String path, @RequestParam("firstValue") final String firstValue) {
        final Selector selector = new Selector(path, "http://localhost:8080/data/third-values?path=" + path + "&firstValue=" + firstValue + "&secondValue=%s");
        selector.add(new Option("Choose one...", null));

        if (NumberUtils.isNumber(firstValue)) {
            selector.add(new Option(firstValue + " value 1", calculate(1, firstValue)));
            selector.add(new Option(firstValue + " value 2", calculate(2, firstValue)));
            selector.add(new Option(firstValue + " value 3", calculate(3, firstValue)));
        }
        return selector;
    }

    @RequestMapping(value = "/third-values", method = RequestMethod.GET)
    public Selector getThirdValues(@RequestParam(value = "path", required = true) final String path, @RequestParam("firstValue") final String firstValue, @RequestParam("secondValue") final String secondValue) {
        final Selector selector = new Selector(path);
        selector.add(new Option("Choose one...", null));

        if (NumberUtils.isNumber(firstValue) && NumberUtils.isNumber(secondValue)) {
            selector.add(new Option(firstValue + " " + secondValue + " value 1", calculate(1, firstValue, secondValue)));
            selector.add(new Option(firstValue + " " + secondValue + " value 2", calculate(2, firstValue, secondValue)));
            selector.add(new Option(firstValue + " " + secondValue + " value 3", calculate(3, firstValue, secondValue)));
        }

        return selector;
    }

    private int calculate(final int value, final String... values) {
        int sum = value;
        for (String stringValue : values) {
            sum += NumberUtils.toInt(stringValue);
        }
        return sum;
    }
}
