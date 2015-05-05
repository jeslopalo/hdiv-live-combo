package es.sandbox.app.web;

import es.sandbox.app.web.control.Transformer;
import es.sandbox.app.web.control.Transformer.NopTransformer;
import es.sandbox.ui.messages.Flash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author jeslopalo
 * @since 24/04/15.
 */
@Controller
@RequestMapping("/live-combos")
public class LiveCombosController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiveCombosController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String get(final Model model) {
        return get(model, new LiveCombosForm());
    }

    public String get(final Model model, final LiveCombosForm form) {
        LOGGER.debug("Loading form {}...", form);
        final Transformer transformer = new NopTransformer();

        model.addAttribute("liveCombosForm", form);
        model.addAttribute("firstValues", new ReferenceDataController(transformer).getFirstValues("firstValue", "secondValue"));
        model.addAttribute("secondValues", new ReferenceDataController(transformer).getSecondValues("secondValue", "thirdValue", form.getFirstValue()));
        model.addAttribute("thirdValues", new ReferenceDataController(transformer).getThirdValues("thirdValue", form.getFirstValue(), form.getSecondValue()));
        return "live-combos";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post(final Flash flash, final Model model, @Valid final LiveCombosForm liveCombosForm, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            LOGGER.warn("Form errors has been detected: {}", bindingResult);
            return get(model, liveCombosForm);
        }

        flash.success("LiveCombosController.post.success");
        flash.success("LiveCombosController.post.selection", liveCombosForm.getFirstValue(), liveCombosForm.getSecondValue(), liveCombosForm.getThirdValue());
        LOGGER.debug("Form has been validated: {}", liveCombosForm);
        return "redirect:/";
    }
}
