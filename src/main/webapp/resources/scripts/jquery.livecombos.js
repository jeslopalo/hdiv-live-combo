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
    var urlsToPopulateCombos = {};

    function setUrlsFor(path, urls) {
        urlsToPopulateCombos[path] = urls || {};
    };

    function getUrlFor(path, selectedOption) {
        if (urlsToPopulateCombos && urlsToPopulateCombos[path]) {
            return urlsToPopulateCombos[path][selectedOption] || null;
        }
    };

    // The actual plugin constructor
    function LiveComboPlugin(element, options) {
        var plugin = this;

        plugin.$element = $(element);
        plugin.name = plugin.$element.attr("name");
        plugin.options = $.extend({}, defaults, options);

        // Main plugin work
        plugin.populateTargetSelector = function () {
            var selectedOptionValue = plugin.getSelectedOptionValue(this);
            var selectedValueUrl = getUrlFor(plugin.name, selectedOptionValue);

            if (selectedValueUrl) {
                $.getJSON(selectedValueUrl, function (data) {
                    updateForm(plugin, data);
                });
            }
        };

        plugin.init();
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

    LiveComboPlugin.prototype.getSelectedOptionValue = function (selector) {
        if (this.options.selectedOptionValue) {
            return this.options.selectedOptionValue($(selector).find("option:selected")) || "";
        }
    };

    function updateForm(plugin, data) {

        plugin.updateFormCsrf(data.csrf);
        populateSelectorOptions(data.path, data.options);
        setUrlsFor(data.path, data.urls);
        fireChangeEventOnTargetSelectorIfRequired(data.path, plugin.options);
    }

    function targetSelector(name) {
        return $("#" + name);
    };

    function populateSelectorOptions(name, options) {
        var target = targetSelector(name).empty();
        $.each(options, function (index, option) {
            target.append("<option value='" + option.value + "' data-value=" + option.dataValue + ">" + option.label + "</option>");
        });
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
