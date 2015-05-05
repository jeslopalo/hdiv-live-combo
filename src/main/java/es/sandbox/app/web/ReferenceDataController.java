package es.sandbox.app.web;

import es.sandbox.app.web.control.HdivTransformer;
import es.sandbox.app.web.control.Option;
import es.sandbox.app.web.control.Selector;
import es.sandbox.app.web.control.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

import static es.sandbox.app.web.control.Selector.SelectorBuilder;
import static es.sandbox.app.web.control.Selector.builderForPath;

/**
 * @author jeslopalo
 * @since 24/04/15.
 */
@RestController
@RequestMapping("/data")
public class ReferenceDataController implements ServletContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataController.class);

    private final Transformer transformer;
    private ServletContext servletContext;

    public ReferenceDataController() {
        this.transformer = null;
    }

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ReferenceDataController(final Transformer transformer) {
        this.transformer = transformer;
    }

    private Transformer transformer() {
        return this.transformer == null? new HdivTransformer(this.servletContext) : this.transformer;
    }

    @RequestMapping(value = "/first-values", method = RequestMethod.GET)
    public Selector getFirstValues(@RequestParam(value = "path", required = true) final String path, @RequestParam(value = "populate-path", required = true) final String populatePath) {
        LOGGER.debug("Loading options for [{}] path and on select populate [{}] path...", path, populatePath);

        return builderForPath(path)
                .onSelect(populatePath, "http://localhost:8080/data/second-values?path=" + populatePath + "&populate-path=thirdValue&firstValue=%s")
                .withTransformer(transformer())
                .withUnselectedOptionLabel("Choose one...")
                .withOptions(
                        new Option("Car", "car"),
                        new Option("Motorbike", "motorbike"))
                .build();
    }

    @RequestMapping(value = "/second-values", method = RequestMethod.GET)
    public Selector getSecondValues(@RequestParam(value = "path", required = true) final String path, @RequestParam(value = "populate-path", required = true) final String populatePath, @RequestParam("firstValue") final String firstValue) {
        LOGGER.debug("Loading options for [{}] path and on select populate [{}] path (selected firstValue=[{}])...", path, populatePath, firstValue);

        final SelectorBuilder selectorBuilder = builderForPath(path)
                .onSelect(populatePath, "http://localhost:8080/data/third-values?path=" + populatePath + "&firstValue=" + firstValue + "&secondValue=%s")
                .withTransformer(transformer())
                .withUnselectedOptionLabel("Choose one...");

        if (firstValue != null) {
            switch (firstValue) {
                case "car":
                    selectorBuilder
                            .addOption(new Option("Ford", "ford"))
                            .addOption(new Option("Citröen", "citroen"))
                            .addOption(new Option("Fiat", "fiat"));
                    break;
                case "motorbike":
                    selectorBuilder
                            .addOption(new Option("Yamaha", "yamaha"))
                            .addOption(new Option("Honda", "honda"))
                            .addOption(new Option("Kymco", "kymco"))
                            .addOption(new Option("Harley-Davidson", "harley"));
                    break;
                default:
                    break;
            }
        }

        return selectorBuilder.build();
    }

    @RequestMapping(value = "/third-values", method = RequestMethod.GET)
    public Selector getThirdValues(@RequestParam(value = "path", required = true) final String path, @RequestParam("firstValue") final String firstValue, @RequestParam("secondValue") final String secondValue) {
        LOGGER.debug("Loading options for [{}] path (selected firstValue=[{}], secondValue=[{}])...", path, firstValue, secondValue);

        final SelectorBuilder selectorBuilder = builderForPath(path)
                .withTransformer(transformer())
                .withUnselectedOptionLabel("Choose one...");

        if (firstValue != null && secondValue != null) {
            switch (firstValue + "-" + secondValue) {
                case "car-ford":
                    selectorBuilder
                            .addOption(new Option("Focus", "focus"))
                            .addOption(new Option("Mustang", "mustang"))
                            .addOption(new Option("Fiesta", "fiesta"));
                    break;
                case "car-citroen":
                    selectorBuilder
                            .addOption(new Option("C4 Picasso", "c4picasso"))
                            .addOption(new Option("C3", "c3"));
                    break;
                case "car-fiat":
                    selectorBuilder
                            .addOption(new Option("Panda", "panda"))
                            .addOption(new Option("Punto", "punto"))
                            .addOption(new Option("Bravo", "bravo"));
                    break;
                case "motorbike-yamaha":
                    selectorBuilder
                            .addOption(new Option("TMax", "tmax"))
                            .addOption(new Option("XMax 250", "xmax250"))
                            .addOption(new Option("XMax 400", "xmax400"));
                    break;
                case "motorbike-honda":
                    selectorBuilder
                            .addOption(new Option("Forza 125", "forza125"))
                            .addOption(new Option("Integra", "integra"))
                            .addOption(new Option("CBR500R", "cbr500r"));
                    break;
                case "motorbike-kymco":
                    selectorBuilder
                            .addOption(new Option("Grand Dink 125", "granddink125"))
                            .addOption(new Option("Xciting", "xciting"));
                    break;
                case "motorbike-harley":
                    selectorBuilder
                            .addOption(new Option("Road King Classic", "roadkingclassic"))
                            .addOption(new Option("883 Roadster", "883roadster"))
                            .addOption(new Option("Fat Bob", "fatbob"))
                            .addOption(new Option("SuperLow", "superlow"));
                    break;
                default:
                    break;
            }
        }

        return selectorBuilder.build();
    }
}
