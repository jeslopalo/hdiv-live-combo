package es.sandbox.app.web;

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
 * Created by jeslopalo on 24/04/15.
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
        model.addAttribute("form", form);
        model.addAttribute("firstValues", new ReferenceDataController().getFirstValues("firstValue"));
        model.addAttribute("secondValues", new ReferenceDataController().getSecondValues("secondValue", form.getFirstValue()));
        model.addAttribute("thirdValues", new ReferenceDataController().getThirdValues("thirdValue", form.getFirstValue(), form.getSecondValue()));
        return "live-combos";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post(final Flash flash, final Model model, @Valid final LiveCombosForm form, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            LOGGER.warn("Form errors has been detected: {}", bindingResult);
            return get(model, form);
        }

        flash.success("LiveCombosController.post.success");
        LOGGER.debug("Form has been validated: {}", form);
        return "redirect:/";
    }
}
