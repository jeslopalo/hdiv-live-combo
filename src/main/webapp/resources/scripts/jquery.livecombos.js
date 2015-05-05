/*!
 * jQuery livecombo plugin
 * Original author: @jeslopalo
 * Licensed under the MIT license
 */
;
(function ($, window, document, undefined) {

    // Create the defaults once
    var pluginName = 'liveCombo',
        defaults = {
            urls: {},
            fireChangeEventOnTargetSelector: true,
            selectedOptionValue: function (option) {
                return option.data("value") || "";
            },
            csrfInputSelector: "form input[type=hidden][name!=_csrf]:last"
        };

    // Define urls
    var _urls = {};

    function setUrlsFor(path, urls) {
        _urls[path] = urls || {};
    };

    function getUrlFor(path, selectedOption) {
        if (_urls && _urls[path]) {
            return _urls[path][selectedOption] || null;
        }
    };

    // The actual plugin constructor
    function LiveComboPlugin(element, options) {
        var base = this;

        base.$element = $(element);
        base.name = base.$element.attr("name");
        base.options = $.extend({}, defaults, options);

        // Main function
        base.populateTargetSelector = function () {
            var selectedOptionValue = base.getSelectedOptionValue(this);
            var selectedValueUrl = getUrlFor(base.name, selectedOptionValue);

            if (selectedValueUrl) {
                $.getJSON(selectedValueUrl, function (data) {
                    base.updateFormCsrf(data.csrf);
                    populateSelector(data.path, data.options);
                    setUrlsFor(data.path, data.urls);
                    fireChangeEventOnTargetSelectorIfRequired(data.path, base.options);
                });
            }
        };

        base.init();
    };

    LiveComboPlugin.prototype.init = function () {
        setUrlsFor(this.name, this.options.urls);

        this.$element.change(this.populateTargetSelector);
    };

    LiveComboPlugin.prototype.updateFormCsrf = function (csrf) {
        if (csrf) {
            $(this.options.csrfInputSelector).val(csrf);
        }
    };

    function targetSelector(name) {
        return $("#" + name);
    };

    function populateSelector(name, options) {
        var target = targetSelector(name).empty();
        $.each(options, function (index, option) {
            target.append("<option value='" + option.value + "' data-value=" + option.dataValue + ">" + option.label + "</option>");
        });
    };

    LiveComboPlugin.prototype.getSelectedOptionValue = function (selector) {
        if (this.options.selectedOptionValue) {
            return this.options.selectedOptionValue($(selector).find("option:selected")) || "";
        }
    };

    function fireChangeEventOnTargetSelectorIfRequired(name, options) {
        if (options.fireChangeEventOnTargetSelector === true) {
            targetSelector(name).change();
        }
    };


    $.fn[pluginName] = function (options) {
        return this.filter("select").each(function () {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName, new LiveComboPlugin(this, options));
            }
        });
    };

})(jQuery, window, document);
