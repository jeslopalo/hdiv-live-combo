(function ($) {

    $.fn.liveCombo = function (options) {

        //merge default and user parameters
        var settings = $.extend({
            urls: {},
            fireChangeEventOnTargetSelector: true,
            selectedOptionValue: function (option) {
                return option.data("value") || "";
            }
        }, options);

        this.filter("select").change(function () {
            var urls = settings.urls;

            var selectorName = $(this).attr("name");
            var selectedOptionValue = getSelectedOptionValue(this);

            var selectedValueUrl = findUrl(urls, selectedOptionValue);
            if (selectedValueUrl) {
                $.getJSON(selectedValueUrl, function (data) {
                    if (data.csrf != null) {
                        var hdivFormStateHiddenInput = $("form input[type=hidden][name!=_csrf]").last();
                        hdivFormStateHiddenInput.val(data.csrf);
                    }

                    var targetControl = $("#" + data.path).empty();
                    $.each(data.options, function (index, option) {
                        targetControl.append("<option value='" + option.value + "' data-value=" + option.dataValue + ">" + option.label + "</option>");
                    });
                    urls = data.urls;

                    fireChangeEventOnTargetSelectorIfRequired(targetControl);
                });
            }
        });

        function findUrl(urls, selectedOption) {
            if (urls) {
                return urls[selectedOption] || null;
            }
            return null;
        }

        function getSelectedOptionValue(selector) {
            return settings.selectedOptionValue($(selector).find("option:selected")) || "";
        }

        function fireChangeEventOnTargetSelectorIfRequired(targetControl) {
            if (settings.fireChangeEventOnTargetSelector) {
                targetControl.change();
            }
        }

        return this;
    };

}(jQuery));
