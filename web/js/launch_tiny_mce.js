tinyMCE.init({
	mode : "textareas",
	theme : "advanced",
	//plugins : "media",
	theme_advanced_buttons1 : "bold,italic,underline,strikethrough,"
                             +",justifyleft,justifycenter,justifyright,"
                             +"justifyfull,|,bullist,numlist,"
                             +"|,undo,redo"
                             +",|,link,unlink,image",
	//theme_advanced_buttons1_add : "media",
	theme_advanced_buttons2 : "formatselect,fontselect,fontsizeselect,|,sub,sup",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	auto_resize : true,
	gecko_spellcheck : true,
	language : "fr",
	docs_language : "fr",
    theme_advanced_resizing : true
});
