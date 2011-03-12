QUEST=1;

(function() {
	x = 1;
	
	// function get(url, args, callback) {
	// 	$.ajax({url: "/api/" + url, username: creds.user, password: creds.key, data: args, success: callback});
	// }
	
	window.do_init_api = function(map) {
			
		$.getJSON("/api/quests/" + QUEST + "/steps/next_seq", function(data) {
			x = data;
		});
		
		$.getJSON("/api/quests/" + QUEST + "/steps", function(data) {
			$.each(data, function(x, step) {
				var point = new GLatLng(step.lat, step.lon);
				
				marker = new GMarker(point, {
					draggable: true,
					title: step.seq + ': ' + step.clue
				});
				
				GEvent.addListener(marker, "dragend", function(point) {
					$.ajax({
						url: "/api/quests/" + QUEST + "/steps/" + step.id,
						type: 'PUT',
						data: {
							step: {
								lat: point.lat(),
								lon: point.lng()
							}
						}
					});
				});
				
				map.addOverlay(marker);
			});
		});
		
		GEvent.addListener(map, "click", function(overlay, point) {
			marker = new GMarker(point, {
				draggable: true,
				title: 'Step ' + x
			});
			
			map.addOverlay(marker);
			
			marker.openInfoWindowHtml(
				'<h1>Step '+ x +'</h1><br/>Clue <input type="text" value="" id="clue_' + x + '"/><br/><button onClick="onSaveStep_'+x+'('+x+');">Save</button>'
			);
			
			window['onSaveStep_'+x] = function(x) {
				clue = $('#clue_' + x).val();
				
				$.post("/api/quests/" + QUEST + "/steps", {
					step: {
						lat: point.y,
						lon: point.x,
						clue: clue,
						error_radius: 1.0}},
					function(result) {
						marker.closeInfoWindow();
					});
			};
			
			x++;
		});
	};
})();