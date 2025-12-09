
	(function($) {
		$(document).ready(																		
			function() {
				$(".expand").click(
					function() {
						$(".expand").each(
							function() {
								$(this).attr('style','width:97%;float:left;min-height:65px;height:65px;');
							}
						);
						$(this).attr('style', 'width:97%;min-height:65px;height:100%;float:left;margin-bottom:3px');
					}
				);
			}
		);
	}(jQuery));

