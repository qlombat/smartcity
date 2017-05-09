//SVG generator
var rsr = Snap("#city-map-svg");
var bg = rsr.path("M.5.5H1278.77V1005.13H.5Z").attr({
    fill: '#6aa84f',
    parent: 'bg',
    'stroke-width': '0',
    'stroke-opacity': '1',
    'id': 'bg',
    'name': 'bg'
}).transform("t-0.5 -0.5").data('id', 'Ground');
var Road = Snap.set();
var path_b = rsr.path("M1.07,400.92H1279.32V604.39H1.07Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_b');
var path_c = rsr.path("M104,841.35H1176.4V980.69H104Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_c');
var path_d = rsr.path("M243.32,25.35V980.69H104V25.35Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_d');
var path_e = rsr.path("M1176.39,25V980.31H1037V25Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_e');
var path_f = rsr.path("M569.72,979.26V24.93H709.06V979.26Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_f');
var path_g = rsr.path("M741.78,401.12H709.06v-77.2h32.72Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_g');
var path_h = rsr.path("M103.2,24.62H1175.58V164H103.2Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_h');
var path_i = rsr.path("M243.32,718.44h38.83v61.92H243.32Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_i');
var path_j = rsr.path("M282.18,665H444.92v152.6H282.18Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_j');
var path_k = rsr.path("M539.5,679.57c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_k');
var path_l = rsr.path("M539.5,675.69c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_l');
var path_m = rsr.path("M539.5,671.76c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_m');
var path_n = rsr.path("M539.5,667.84c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_n');
var path_o = rsr.path("M539.5,664c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_o');
var path_p = rsr.path("M539.5,660.07c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_p');
var path_q = rsr.path("M539.5,656.19c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_q');
var path_r = rsr.path("M539.5,652.26c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_r');
var path_s = rsr.path("M539.5,648.32c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_s');
var path_t = rsr.path("M539.5,644.51c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_t');
var path_u = rsr.path("M539.5,640.84c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_u');
var path_v = rsr.path("M539.5,636.94c-36.41,0-54.61-19.23-72.82-38.46S430.27,560,393.86,560").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_v');
var path_w = rsr.path("M539.5,633c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_w');
var path_x = rsr.path("M539.5,629.07c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_x');
var path_y = rsr.path("M539.5,625.26c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_y');
var path_z = rsr.path("M539.5,621.32c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_z');
var path_aa = rsr.path("M539.5,617.44c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_aa');
var path_ab = rsr.path("M539.5,613.51c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ab');
var path_ac = rsr.path("M539.5,609.57c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ac');
var path_ad = rsr.path("M539.5,605.76c-36.41,0-54.61-19.23-72.82-38.46s-36.41-38.46-72.82-38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ad');
var path_ae = rsr.path("M538.65,604.39h32.72v77.2H538.65Z").attr({
    fill: '#b7b7b7',
    parent: 'Road',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ae');
var path_af = rsr.path("M740.93,325.94c36.41,0,54.61,19.23,72.82,38.46s36.41,38.44,72.82,38.44").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_af');
var path_ag = rsr.path("M740.93,329.84c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ag');
var path_ah = rsr.path("M740.93,333.75c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ah');
var path_ai = rsr.path("M740.93,337.69c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ai');
var path_aj = rsr.path("M740.93,341.49c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_aj');
var path_ak = rsr.path("M740.93,345.44c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ak');
var path_al = rsr.path("M740.93,349.32c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_al');
var path_am = rsr.path("M740.93,353.25c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_am');
var path_an = rsr.path("M740.93,357.19c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_an');
var path_ao = rsr.path("M740.93,361c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ao');
var path_ap = rsr.path("M740.93,364.69c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ap');
var path_aq = rsr.path("M740.93,368.57c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_aq');
var path_ar = rsr.path("M740.93,372.5c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ar');
var path_as = rsr.path("M740.93,376.44c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_as');
var path_at = rsr.path("M740.93,380.25c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_at');
var path_au = rsr.path("M740.93,384.19c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_au');
var path_av = rsr.path("M740.93,388.07c36.41,0,54.61,19.23,72.82,38.46S850.16,465,886.57,465").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_av');
var path_aw = rsr.path("M740.93,392c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_aw');
var path_ax = rsr.path("M740.93,395.94c36.41,0,54.61,19.23,72.82,38.46s36.41,38.44,72.82,38.44").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ax');
var path_ay = rsr.path("M740.93,399.75c36.41,0,54.61,19.23,72.82,38.46s36.41,38.46,72.82,38.46").attr({
    fill: 'none',
    stroke: '#b7b7b7',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Road',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ay');
Road.attr({'id': 'Road', 'name': 'Road'});
var Bande = Snap.set();
var DottedLineWest = rsr.path("M1.07,464.93H386.49").attr({
    id: 'DottedLineWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineWest');
var LineWest = rsr.path("M1.07,541.54H393.86m144.85-77h31M393.86,541.45c36.41,0,54.61-19.23,72.82-38.46s36.41-38.46,72.82-38.46").attr({
    id: 'LineWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'LineWest');
var path_az = rsr.path("M538.65,541.57l31.09-.06").attr({
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_az');
var path_ba = rsr.path("M538.65,604.24l31.09-.06").attr({
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_ba');
var ArrwoNorthWest = rsr.path("M510.78,511.35h35.61V494.7h-4.16l8.32-8.32,8.32,8.32h-4.16v25H510.78Z").attr({
    id: 'ArrwoNorthWest',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'ArrwoNorthWest');
var ArrowSouthWest = rsr.path("M510.78,636.45h35.61V653.1h-4.16l8.32,8.32,8.32-8.32h-4.16v-25H510.78Z").attr({
    id: 'ArrowSouthWest',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'ArrowSouthWest');
var StopMainWest = rsr.path("M567.72,462.84V606.15").attr({
    id: 'StopMainWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopMainWest');
var GiveAwayNorth = rsr.path("M569.72,612.58l-5.54-3.21,5.54-3.21Zm0,6.42-5.55-3.16,5.54-3.21Zm0,6.43-5.54-3.21,5.54-3.21Zm0,6.41-5.54-3.21,5.54-3.21Zm0,6.44-5.54-3.21,5.54-3.23Zm0,6.42-5.54-3.21,5.54-3.21Zm0,6.43-5.54-3.21,5.54-3.21Zm0,6.43-5.54-3.21,5.54-3.21Zm0,6.42-5.54-3.21,5.54-3.21Zm0,6.43-5.54-3.21,5.54-3.21Zm0,6.43-5.54-3.21,5.54-3.21Zm0,6.42L564.18,680l5.54-3.21Z").attr({
    id: 'GiveAwayNorth',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'GiveAwayNorth');

var DottedLineEast = rsr.path("M1279.32,540.58H893.94").attr({
    id: 'DottedLineEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineEast');
var LineEast = rsr.path("M1279.32,464H886.57M741.72,541h-31m175.85-76.91c-36.41,0-54.61,19.23-72.82,38.46S777.32,541,740.93,541").attr({
    id: 'LineEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'LineEast');
var path_bb = rsr.path("M741.78,463.94l-31.09.06").attr({
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_bb');
var path_bc = rsr.path("M741.78,401.27l-31.09.06").attr({
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'path_bc');
var ArrowSouthEast = rsr.path("M769.65,494.16H734v16.68h4.16l-8.32,8.32-8.33-8.32h4.16v-25h43.93Z").attr({
    id: 'ArrowSouthEast',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'ArrowSouthEast');
var ArrowNorthEast = rsr.path("M769.65,369.06H734V352.42h4.16l-8.32-8.32-8.32,8.32h4.16v25h43.93Z").attr({
    id: 'ArrowNorthEast',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'ArrowNorthEast');
var StopMainEast = rsr.path("M712.71,542.66V399.36").attr({
    id: 'StopMainEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopMainEast');
var GiveAwayNorth2 = rsr.path("M710.71,392.93l5.54,3.21-5.54,3.21Zm0-6.42,5.54,3.21-5.54,3.21Zm0-6.43,5.54,3.21-5.54,3.21Zm0-6.43,5.54,3.21-5.54,3.21Zm0-6.42,5.54,3.21-5.54,3.21Zm0-6.39,5.54,3.21-5.54,3.21Zm0-6.46,5.54,3.21-5.54,3.25Zm0-6.43,5.54,3.21-5.54,3.21Zm0-6.42,5.54,3.21-5.54,3.21Zm0-6.43,5.54,3.21-5.54,3.21Zm0-6.42,5.54,3.21-5.54,3.21Zm0-6.43,5.54,3.21-5.54,3.21Z").attr({
    id: 'GiveAwayNorth-2',
    "data-name": 'GiveAwayNorth',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'GiveAwayNorth2');
var Pedestrian_North = rsr.path("M692.08,277.11h15.06v42.55H692.08Zm-30.11,0h15v42.55H662Zm-30.11,0h15.06v42.55H631.86Zm-60.22,0H586.7v42.55H571.64Zm30.11,0h15.06v42.55H601.75Z").attr({
    id: 'Pedestrian_North',
    "data-name": 'Pedestrian North',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'Pedestrian_North');
var Pedestrian_South = rsr.path("M692.08,684.36h15.06v42.55H692.08Zm-30.11,0h15v42.55H662Zm-30.11,0h15.06v42.55H631.86Zm-60.22,0H586.7v42.55H571.64Zm30.11,0h15.06v42.55H601.75Z").attr({
    id: 'Pedestrian_South',
    "data-name": 'Pedestrian South',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'Pedestrian_South');
var DottedLineNorth = rsr.path("M1107.72,94.29H174.84").attr({
    id: 'DottedLineNorth',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineNorth');
var DottedLineNorthWest = rsr.path("M173.68,96.08v304").attr({
    id: 'DottedLineNorthWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineNorthWest');
var DottedLineNorthEast = rsr.path("M1106.72,95.61v304").attr({
    id: 'DottedLineNorthEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineNorthEast');
var DottedLineNorthCenter = rsr.path("M639.39,164v93.88").attr({
    id: 'DottedLineNorthCenter',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineNorthCenter');
var StopNorthToSouthCenter = rsr.path("M641.45,255.91H569.73").attr({
    id: 'StopNorthToSouthCenter',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopNorthToSouthCenter');
var StopNorthToSouthWest = rsr.path("M175.73,398.84H104").attr({
    id: 'StopNorthToSouthWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopNorthToSouthWest');
var StopNorthToSouthEast = rsr.path("M1108.77,398.84H1037").attr({
    id: 'StopNorthToSouthEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopNorthToSouthEast');
var DottedLineSouth = rsr.path("M172.69,911h932.88").attr({
    id: 'DottedLineSouth',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineSouth');
var DottedLineSouthEast = rsr.path("M1106.73,909.23v-304").attr({
    id: 'DottedLineSouthEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineSouthEast');
var DottedLineSouthWest = rsr.path("M173.69,909.7v-304").attr({
    id: 'DottedLineSouthWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineSouthWest');
var DottedLineSouthCenter = rsr.path("M641,841.22v-93.8").attr({
    id: 'DottedLineSouthCenter',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    "stroke-dasharray": '32 12',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'DottedLineSouthCenter');
var StopSouthToNorthCenter = rsr.path("M639,749.4h70.17").attr({
    id: 'StopSouthToNorthCenter',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopSouthToNorthCenter');
var StopSouthToNorthEast = rsr.path("M1104.68,606.52h71.72").attr({
    id: 'StopSouthToNorthEast',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopSouthToNorthEast');
var StopSouthToNorthWest = rsr.path("M171.64,606.52h71.68").attr({
    id: 'StopSouthToNorthWest',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopSouthToNorthWest');
var Parking = rsr.path("M346.87,764.23V718.42h14.84q8.44,0,11,.69a12,12,0,0,1,6.59,4.48,14.22,14.22,0,0,1,2.66,8.92,15,15,0,0,1-1.53,7.09,12.56,12.56,0,0,1-3.89,4.52,13.41,13.41,0,0,1-4.8,2.17,52.79,52.79,0,0,1-9.59.66h-6v17.28Zm9.25-38.06v13h5.06q5.47,0,7.31-.72a6.07,6.07,0,0,0,2.89-2.25,6.16,6.16,0,0,0,1-3.56,5.92,5.92,0,0,0-1.47-4.12,6.4,6.4,0,0,0-3.72-2,44.47,44.47,0,0,0-6.66-.31Z").attr({
    id: 'Parking',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'Parking');
var StopNorthToSouthCenter2 = rsr.path("M569.72,839.27h70.17").attr({
    id: 'StopNorthToSouthCenter-2',
    "data-name": 'StopNorthToSouthCenter',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopNorthToSouthCenter2');
var StopSouthToNorthCenter2 = rsr.path("M639,165.84h70.17").attr({
    id: 'StopSouthToNorthCenter-2',
    "data-name": 'StopSouthToNorthCenter',
    fill: 'none',
    stroke: '#fff',
    "stroke-linejoin": 'round',
    "stroke-width": '4',
    parent: 'Bande',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'StopSouthToNorthCenter2');
var Bus = rsr.path("M338.16,442.34h-4.92a13.11,13.11,0,0,1-3.53-.36,3.46,3.46,0,0,1-1.94-1.47A6.52,6.52,0,0,1,327,437a3.8,3.8,0,0,1,.53-2.33,3.56,3.56,0,0,1,2-1,3.63,3.63,0,0,1-2.31-1.3,4.9,4.9,0,0,1-.61-2.73v-1.75a7,7,0,0,1,.44-2.84,2.38,2.38,0,0,1,1.41-1.27,14.17,14.17,0,0,1,3.94-.34h5.7Zm-4.94-3.23v-4.27h-.48q-.73,0-.92.36a5.76,5.76,0,0,0-.2,2,3.18,3.18,0,0,0,.17,1.25.76.76,0,0,0,.42.45,3.7,3.7,0,0,0,1,.2Zm0-7.17v-5.31a2.05,2.05,0,0,0-1.33.33,2.34,2.34,0,0,0-.28,1.44v1.77q0,1.22.27,1.47a2.23,2.23,0,0,0,1.34.29Zm-19.54,10.4V429.66a22,22,0,0,1,.14-3,4.07,4.07,0,0,1,.84-1.8,4.36,4.36,0,0,1,1.81-1.41,6.92,6.92,0,0,1,2.67-.47,7.55,7.55,0,0,1,3,.56,4.64,4.64,0,0,1,1.94,1.47,4.17,4.17,0,0,1,.77,1.91q.11,1,.11,4.22v11.2H320V428.12a5.41,5.41,0,0,0-.14-1.59.54.54,0,0,0-.55-.34.55.55,0,0,0-.59.38,7,7,0,0,0-.14,1.81v14Zm-12.36-5.74h4.58V438a2.58,2.58,0,0,0,.19,1.25.64.64,0,0,0,.58.27.75.75,0,0,0,.67-.36,2,2,0,0,0,.23-1.09,3.07,3.07,0,0,0-.27-1.44,3.71,3.71,0,0,0-1.39-1.16,14,14,0,0,1-4.16-3.22,7.4,7.4,0,0,1-.84-4,7.24,7.24,0,0,1,.47-3,3.72,3.72,0,0,1,1.83-1.61,7.22,7.22,0,0,1,3.17-.66,7.12,7.12,0,0,1,3.39.75,3.77,3.77,0,0,1,1.83,1.92A10,10,0,0,1,312,429v1.23h-4.58v-2.35a3.17,3.17,0,0,0-.19-1.37.77.77,0,0,0-.69-.3.79.79,0,0,0-.73.39,2.25,2.25,0,0,0-.23,1.14,3.61,3.61,0,0,0,.45,2.2,14.43,14.43,0,0,0,2.31,1.72,25.15,25.15,0,0,1,2.44,1.77,4.11,4.11,0,0,1,1,1.52,6.84,6.84,0,0,1,.39,2.5,6.92,6.92,0,0,1-.55,3.17,3.74,3.74,0,0,1-1.8,1.58,7.14,7.14,0,0,1-3,.58,7.71,7.71,0,0,1-3.27-.62,3.44,3.44,0,0,1-1.8-1.56,8.28,8.28,0,0,1-.44-3.2Z").attr({
    id: 'Bus',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'Bus');
var Bus2 = rsr.path("M942.25,563.17h4.92a13.11,13.11,0,0,1,3.53.36,3.46,3.46,0,0,1,1.94,1.47,6.52,6.52,0,0,1,.75,3.56,3.8,3.8,0,0,1-.53,2.33,3.56,3.56,0,0,1-2,1,3.63,3.63,0,0,1,2.31,1.3,4.9,4.9,0,0,1,.61,2.73v1.77a7,7,0,0,1-.44,2.84,2.38,2.38,0,0,1-1.41,1.27,14.16,14.16,0,0,1-3.94.34h-5.7Zm4.94,3.23v4.22h.48q.73,0,.92-.36a5.76,5.76,0,0,0,.2-2,3.18,3.18,0,0,0-.17-1.25.76.76,0,0,0-.42-.45,3.71,3.71,0,0,0-1-.15Zm0,7.17v5.31a2,2,0,0,0,1.33-.33,2.34,2.34,0,0,0,.28-1.44v-1.77q0-1.22-.27-1.47a2.23,2.23,0,0,0-1.34-.29Zm19.54-10.41v12.68a22.05,22.05,0,0,1-.14,3,4.07,4.07,0,0,1-.84,1.8,4.36,4.36,0,0,1-1.81,1.41,6.92,6.92,0,0,1-2.67.47,7.55,7.55,0,0,1-3-.56,4.64,4.64,0,0,1-1.94-1.47,4.17,4.17,0,0,1-.77-1.91q-.11-1-.11-4.22V563.17h4.87v14.22a5.41,5.41,0,0,0,.14,1.59.54.54,0,0,0,.55.34.55.55,0,0,0,.59-.37,7,7,0,0,0,.14-1.81v-14Zm12.36,5.73h-4.58v-1.41a2.58,2.58,0,0,0-.19-1.25.64.64,0,0,0-.58-.27.75.75,0,0,0-.67.36,2,2,0,0,0-.23,1.09,3.07,3.07,0,0,0,.27,1.44A3.71,3.71,0,0,0,974.5,570a14,14,0,0,1,4.16,3.22,7.4,7.4,0,0,1,.84,4,7.24,7.24,0,0,1-.47,3,3.72,3.72,0,0,1-1.83,1.61,7.22,7.22,0,0,1-3.17.66,7.12,7.12,0,0,1-3.39-.75,3.77,3.77,0,0,1-1.83-1.92,10,10,0,0,1-.42-3.3V575.3H973v2.31a3.17,3.17,0,0,0,.19,1.38.77.77,0,0,0,.69.3.79.79,0,0,0,.73-.39,2.25,2.25,0,0,0,.23-1.14,3.61,3.61,0,0,0-.45-2.2,14.44,14.44,0,0,0-2.31-1.72,25.15,25.15,0,0,1-2.44-1.77,4.11,4.11,0,0,1-1-1.52,6.84,6.84,0,0,1-.39-2.5,6.92,6.92,0,0,1,.55-3.17,3.74,3.74,0,0,1,1.8-1.58,7.14,7.14,0,0,1,3-.58,7.71,7.71,0,0,1,3.27.63,3.44,3.44,0,0,1,1.8,1.56,8.28,8.28,0,0,1,.44,3.2Z").attr({
    id: 'Bus-2',
    "data-name": 'Bus',
    fill: '#fff',
    parent: 'Bande',
    'stroke-width': '0',
    'stroke-opacity': '1'
}).transform("t-0.5 -0.5").data('id', 'Bus2');
Bande.attr({'id': 'Bande', 'name': 'Bande'});

var MainCarDetectorWest = rsr.path("M307.32,573.6h0a12.39,12.39,0,0,1,12.39-12.39h0A12.39,12.39,0,0,1,332.1,573.6h0A12.39,12.39,0,0,1,319.71,586h0a12.39,12.39,0,0,1-12.39-12.39Z").attr({
    fill: '#fff',
    parent: 'MainCarDetectorWest',
    'stroke-width': '0',
    'stroke-opacity': '1',
    'id': 'MainCarDetectorWest',
    'name': 'MainCarDetectorWest'
}).transform("t-0.5 -0.5").data('id', 'path_bd');

var MainCarDetectorEast = rsr.path("M948.26,431.91h0a12.39,12.39,0,0,1,12.39-12.39h0A12.39,12.39,0,0,1,973,431.91h0a12.39,12.39,0,0,1-12.39,12.39h0a12.39,12.39,0,0,1-12.39-12.39Z").attr({
    fill: '#fff',
    'stroke-width': '0',
    'stroke-opacity': '1',
    'id': 'MainCarDetectorEast',
    'name': 'MainCarDetectorEast'
}).transform("t-0.5 -0.5").data('id', 'path_bi');

var AuxiliaryCarDetectorNorth = rsr.path("M593,231.61h0a12.39,12.39,0,0,1,12.35-12.39h0a12.39,12.39,0,0,1,12.39,12.39h0A12.39,12.39,0,0,1,605.32,244h0A12.39,12.39,0,0,1,593,231.61Z").attr({
    fill: '#fff',
    'stroke-width': '0',
    'stroke-opacity': '1',
    'id': 'AuxiliaryCarDetectorNorth',
    'name': 'AuxiliaryCarDetectorNorth'
}).transform("t-0.5 -0.5").data('id', 'path_bn');
var AuxiliaryCarDetectorSouth = rsr.path("M662.65,773.7h0A12.39,12.39,0,0,1,675,761.31h0a12.39,12.39,0,0,1,12.39,12.39h0A12.39,12.39,0,0,1,675,786.09h0a12.39,12.39,0,0,1-12.39-12.39Z").attr({
    fill: '#fff',
    'stroke-width': '0',
    'stroke-opacity': '1',
    'id': 'AuxiliaryCarDetectorSouth',
    'name': 'AuxiliaryCarDetectorSouth'
}).transform("t-0.5 -0.5").data('id', 'path_bs');
var RFID = rsr.path("M250.37,696.75h0a12.39,12.39,0,0,1,12.39-12.39h0a12.39,12.39,0,0,1,12.39,12.39h0a12.39,12.39,0,0,1-12.39,12.39h0a12.39,12.39,0,0,1-12.39-12.39Z").attr({
    fill: '#fff',
    'stroke-width': '0',
    'stroke-opacity': '1',
    'id': 'RFID',
    'name': 'RFID'
}).transform("t-0.5 -0.5").data('id', 'path_bx');

Road.push(path_b, path_c, path_d, path_e, path_f, path_g, path_h, path_i, path_j, path_k, path_l, path_m, path_n, path_o, path_p, path_q, path_r, path_s, path_t, path_u, path_v, path_w, path_x, path_y, path_z, path_aa, path_ab, path_ac, path_ad, path_ae, path_af, path_ag, path_ah, path_ai, path_aj, path_ak, path_al, path_am, path_an, path_ao, path_ap, path_aq, path_ar, path_as, path_at, path_au, path_av, path_aw, path_ax, path_ay);
Bande.push(DottedLineWest, LineWest, path_az, path_ba, ArrwoNorthWest, ArrowSouthWest, StopMainWest, GiveAwayNorth, DottedLineEast, LineEast, path_bb, path_bc, ArrowSouthEast, ArrowNorthEast, StopMainEast, GiveAwayNorth2, Pedestrian_North, Pedestrian_South, DottedLineNorth, DottedLineNorthWest, DottedLineNorthEast, DottedLineNorthCenter, StopNorthToSouthCenter, StopNorthToSouthWest, StopNorthToSouthEast, DottedLineSouth, DottedLineSouthEast, DottedLineSouthWest, DottedLineSouthCenter, StopSouthToNorthCenter, StopSouthToNorthEast, StopSouthToNorthWest, Parking, StopNorthToSouthCenter2, StopSouthToNorthCenter2, Bus, Bus2);

RFID.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "RFID"
});
AuxiliaryCarDetectorSouth.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Car detector of South auxiliary road"
});
AuxiliaryCarDetectorNorth.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Car detector of North auxiliary road"
});
MainCarDetectorEast.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Car detector of East main road"
});
MainCarDetectorWest.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Car detector of West main road"
});
Bus.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Traffic band reserved for buses"
});
Bus2.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Traffic band reserved for buses"
});

Parking.attr({
    'data-container': "body",
    'data-toggle': "popover",
    'data-placement': "top",
    'data-content': "Parking"
});


$('[data-toggle="popover"]').popover({trigger: "hover"});
function CityMap(container, map) {
    this.container = container;
    this.map = map;
    this.zoomScale = 1;
    this.minZoomScale = 1;
    this.maxZoomScale = 2.5;
    this.originalHeight = 0;
    this.originalWidth = 0;
    this._top = 0;
    this._left = 0;
    this.init = function () {
        console.log("init()");
        this.originalWidth = this.map.width();
        this.originalHeight = this.map.height();

        //Init container
        this.container.css({
            overflow: 'hidden',
            position: 'relative'
        });

        //Center map in the container
        this.center();
        //Init zoom
        this.container.on("mousewheel", function (event, delta) {
            event.preventDefault();
            delta = delta > 0 ? 0.1 : -0.1;
            this.zoom(delta);
            return false;
        }.bind(this));

        var me = this;
        //Init draggable
        $('#city-map-svg').draggable({
            drag: function (event, ui) {
                console.log(ui);
                me.move(ui);
            }
        });
    };
    this.center = function () {
        console.log("center()");
        var top = (this.container.height() - this.map.height()) / 2;
        var left = (this.container.width() - this.map.width()) / 2;
        this.move(top, left);
    };
    this.zoom = function (zoom) {
        console.log("zoom(zoom)", zoom);

        this.zoomScale += zoom;
        if (this.zoomScale <= this.minZoomScale) {
            this.zoomScale = this.minZoomScale;
            zoom = 0
        } else if (this.zoomScale >= this.maxZoomScale) {
            this.zoomScale = this.maxZoomScale;
            zoom = 0
        }
        this.move(this._top, this._left);
        this.map.css({
            transform: "scale(" + this.zoomScale + ")"
        });

    };
    this.correctLeft = function (value) {
        var minLeft = this.container.width() - ((this.originalWidth / 2) * (this.zoomScale - 1) + this.originalWidth);
        var maxLeft = (this.originalWidth / 2) * this.zoomScale - (this.originalWidth / 2);
        if (value < minLeft) {
            console.log("minLeft", minLeft);
            value = minLeft;
        } else if (value > maxLeft) {
            console.log("maxLeft", maxLeft);
            value = maxLeft;
        }
        return value;
    };
    this.correctTop = function (value) {
        var minTop = this.container.height() - ((this.originalHeight / 2) * (this.zoomScale - 1) + this.originalHeight);
        var maxTop = (this.originalHeight / 2) * this.zoomScale - (this.originalHeight / 2);

        if (value < minTop) {
            console.log("minTop", minTop);
            value = minTop;
        } else if (value > maxTop) {
            console.log("maxTop", maxTop);
            value = maxTop;
        }
        return value;
    };
    this.move = function (a, b) {
        if (arguments.length === 1) {
            console.log("move(ui)", a);
            //a = ui
            this._top = this.correctTop(a.position.top);
            this._left = this.correctLeft(a.position.left);
            a.position.top = this._top;
            a.position.left = this._left;
        } else if (arguments.length === 2) {
            console.log("move(top, left)", a, b);
            this._top = this.correctTop(a);
            this._left = this.correctLeft(b);
            this.map.css({
                top: this._top,
                left: this._left
            });
        }
        console.log("position", this._top, this._left);
    };
}


var cityMap = new CityMap($("#city-map"), $("#city-map-svg"));
cityMap.init();