"use strict"
var emo_to_img = {};
emo_to_img[':)'] = emo_path('smile_20.png');
emo_to_img[':=)'] = emo_path('smile_20.png');
emo_to_img[':-)'] = emo_path('smile_20.png');

//:D:=D:-D:d:=d:-d
emo_to_img[':D'] = emo_path('bigsmile_20.png');
emo_to_img[':=D'] = emo_path('bigsmile_20.png');
emo_to_img[':-D'] = emo_path('bigsmile_20.png');
emo_to_img[':d'] = emo_path('bigsmile_20.png');
emo_to_img[':=d'] = emo_path('bigsmile_20.png');
emo_to_img[':-d'] = emo_path('bigsmile_20.png');

//(h)(H)(l)(L)
emo_to_img['(h)'] = emo_path('heart_20.png');
emo_to_img['(H)'] = emo_path('heart_20.png');
emo_to_img['(l)'] = emo_path('heart_20.png');
emo_to_img['(L)'] = emo_path('heart_20.png');

emo_to_img['(wait)'] = emo_path('wait_20.png');
emo_to_img['(hi)'] = emo_path('hi_20.png');

emo_to_img['(bear)'] = emo_path('hug_20.png');
emo_to_img['(hug)'] = emo_path('hug_20.png');

emo_to_img['(makeup)'] = emo_path('makeup_20.png');
emo_to_img['(kate)'] = emo_path('makeup_20.png');

emo_to_img['(clap)'] = emo_path('clapping_20.png');
emo_to_img['(bow)'] = emo_path('bow_20.png');
emo_to_img['(whew)'] = emo_path('whew_20.png');
emo_to_img['(tmi)'] = emo_path('tmi_20.png');

function escape_with_emoticons(text) {
  var emo_texts = Object.keys(emo_to_img)
  var escaped = text;
  emo_texts.forEach(function(emo_text) {
    escaped = replaceAll(escaped, emo_text, get_img_tag(emo_text))
  });
  return escaped;
}

function replaceAll(text, to_replace, replacement) {
  return text.split(to_replace).join(replacement);
}

function get_img_tag(emo_text) {
  return "<img src='"+ emoticon_for(emo_text) +"' alt='" + emo_text + "'></img>"
}

function emoticon_for(expr) {
  return emo_to_img[expr];
};

function emo_path(emo) {
  return 'img/emoticons/' + emo;
}

function get_all_mappings_by_path() {
  var emo_texts = Object.keys(emo_to_img)
  var unique_mappings = {}
  emo_texts.forEach(function(emo_text) {
    unique_mappings[emo_to_img[emo_text]] = emo_text;
  });
  return unique_mappings;
}