package client;

import client.epuck.CTRNNMultilayer;
import client.epuck.Controller;
import client.epuck.ControllerFrame;
import client.epuck.HierarchicalController;

public class SimpleRemoteController {
	
	public static void main(String[] args) {
		
		CTRNNMultilayer enterCorridor = new CTRNNMultilayer(4, 10, 2, new int[]{0,1,2,3}, "Enter Corridor", new double[]{-0.16350360304124611,1.3440018247809455,-1.039252246734168,-4.568601343277295,2.3457104870097036,0.13140972096837777,7.880152605764118,1.0499969920281107,10.0,-10.0,10.0,-1.1342159920924664,8.037512229694112,-7.162269640373575,6.204423158327488,1.2222908495190077,-9.494014235972529,-5.906746281450354,3.3595052655100126,3.2999873698144784,-5.836250124515049,1.019395795847187,1.7313165048045063,3.5866942629281695,3.524026666377264,-0.3486815022910713,-5.418077044320309,6.6138327348968,-1.0952192205526252,0.3248982966554281,6.241148933107169,-3.270623771541062,-10.0,7.376133320997642,0.2569287508993239,-10.0,1.6751997334569875,-3.9572017121828433,-4.844880701732422,1.7721451493088147,5.2003406985123934,0.8998625304119818,0.9757565430808253,0.5231775043022197,-0.25507598907254325,5.523283334524805,4.703056600211234,-0.11875626022363917,-1.4054408134507548,4.329578022851065,1.2939061977523367,-7.5847527668164085,5.16090348007503,-1.039361582395601,1.5200776262637747,-7.008602538215438,2.3506138213816854,8.535857587994789,-4.953633664439478,-7.97206709514078,-6.280328471819507,2.8116237819517553,0.8540749450864874,0.6377157022761331,-0.9021463287892452,-1.6414016992765728,2.4733604889980936,1.3403544052020204,-6.106097126489438,-2.719469720553491,4.530792293819057,-4.2312664709017715,-9.051745577382503,6.95737081171858,-0.27474771872644915,8.503884152961717,5.152854781851863,7.602222789851348,0.5892702010951698,-4.891034453761941,-4.232061806032782,-1.1574284210608805,8.492466897068255,4.001665773103566,-0.8836170172050368,4.278778230591482,-3.094080020149443,8.31913184036815,7.248807518917257,-9.327191905390524,0.21524797912028315,-2.0087233018374366,-6.582670976447802,-4.178654760756065,1.4973106489698553,-5.2243999731685316,0.4378029248719284,7.951298657688316,-6.525900142495583,-3.7386713884458342,-3.881869130846113,-3.566698173382286,-6.143498568279552,0.11057820610931857,-6.870500226236112,-8.444097152154372,-2.3968047586712222,-1.6268036441424973,5.106017296627328,-7.434731921860306,2.1833976726804885,-5.648852978410369,-7.8519605270297905,0.7233112884941371,-3.333307827235116,0.8923787051853145,4.680701207968462,-0.6267026074029871,-1.8325519311362757,5.43485243328652,6.267685184590469,1.595140343226232,-6.466315461910796,-4.0370135146726716,0.5386674774982789,-8.977945566285861,-10.0,-2.0007832079920314,-1.1676490594624958,4.894589113863869,-5.20177493177313,2.266827841056748,-0.5624075529294126,3.2736283024647292,6.17965564384728,-4.974614660822189,-1.7057785805811254,7.7581110905939745,-1.2645705176510071,1.284127251749646,4.6573217332651105,3.5365234239522216,-0.9426833338382751,5.808663222999608,-3.464612198550816,0.5244342981927046,-6.233849007372674,-6.973024770080239,-1.9043380404135843,-0.6726897400628435,6.822886789972441,5.66878439582644,0.9687337994453346,2.112251906655078,-7.233464792647105,-3.8453535513861876,3.406749702655852,-3.000671847806184,-8.43725346611179,4.738957473806845,-5.675347786247717,-3.112086361013657,-6.9865157740706785,-1.3170554224371323,6.087370565541811,6.9328864992824295,-7.4242518165503615,-1.3074639550957574,3.1851614259460526,-1.0647049149111982,4.178758922770476,1.1077917488344013,-4.844643545455244,-0.9459382572537657,-5.860664473407785,-1.8474992509633694,2.2340076510759017,1.4520047304699188,3.6219720238304665,5.0705092782429695,5.907275974996668,-3.249362573120477});
		
		CTRNNMultilayer left = new CTRNNMultilayer(4, 3, 2, new int[]{0,1,2,3}, "Turn Left", new double[]{9.095773022722224,0.03923895271324235,-7.370912117389587,-0.8707445160021179,7.473311886273157,2.8672891094094624,-4.2774935714223,-2.085010720469337,-0.34557806961392734,-2.0848845149509514,0.8931277035368428,5.319988519991129,-0.8691426520811719,-0.15335162863277968,-0.06474477616494312,7.204049847751121,8.795609470712648,-1.2720420880762169,3.7715695220029835,0.4197629079733215,1.6666022478663218,-0.41585744347598236,-1.2074271217086203,-1.0899123945611389,-8.252283268958678,2.4219815950559767,1.121139076726136,-5.045984953910253,1.280470176700061,2.0152387047664346,9.850931584650137,1.3161923341624073,-1.2907437696082646,0.17761094055107884,-0.4035810667579446});
		CTRNNMultilayer right = new CTRNNMultilayer(4, 3, 2, new int[]{0,1,2,3}, "Turn Right", new double[]{-7.707777742952458,1.4323703870501172,0.2619673679912572,3.4063916789417688,2.4576351917289583,2.773524456403603,-2.4030871778972926,2.787137423896675,4.383114115239691,-4.092280911610402,-0.20961791844444097,5.243897570054747,-1.0227651870464145,-2.1867716213476847,-2.3147315006639513,0.7083119250695245,4.8495975870866985,4.901412296578489,3.824944880027433,-5.0023845791364145,3.1939213263589115,3.6036522385235408,1.11400056956818,-0.46145808854038406,-2.274350781918636,-1.640148960053451,-9.468160560407025,5.8306001443154,-1.3937764558402332,2.317105801023262,0.8904653824163009,0.6138698572412138,-6.1499244467689635,-1.0875202913690907,0.1315072918162985});
		CTRNNMultilayer corridor = new CTRNNMultilayer(4, 3, 2, new int[]{0,1,2,3}, "Corridor", new double[]{4.481480551071801,-1.7939801584587738,-2.1708231054875373,-3.4542911060950376,-2.9060837271978066,1.8139545505969585,2.228501050764975,-4.18102298516704,7.9364751407649505,-0.9738812273184709,-0.010266972472246505,-8.249295096437816,-4.690076484091721,1.632320011215475,3.2212630090085237,0.9552428315284287,2.824529592745838,-1.89571746480064,-3.3626157498178677,0.9905926607383322,2.4094979392431366,-2.0747978918017167,-2.217014393481088,-0.4193795146604231,-4.900513082037346,-3.8657135789399732,-6.07495863680856,-7.864498042707206,7.56719053286604,0.22768158551630036,-1.0206683866245871,2.5640086076524664,6.008600031023879,3.056359479981354,-0.866765544167741});
		
		CTRNNMultilayer room = new CTRNNMultilayer(4,10,2,new int[]{0,1,2,3}, "Exit Room", new double[]{-0.16350360304124611,1.3440018247809455,-1.039252246734168,-4.568601343277295,2.3457104870097036,0.13140972096837777,7.880152605764118,1.0499969920281107,10.0,-10.0,10.0,-1.1342159920924664,8.037512229694112,-7.162269640373575,6.204423158327488,1.2222908495190077,-9.494014235972529,-5.906746281450354,3.3595052655100126,3.2999873698144784,-5.836250124515049,1.019395795847187,1.7313165048045063,3.5866942629281695,3.524026666377264,-0.3486815022910713,-5.418077044320309,6.6138327348968,-1.0952192205526252,0.3248982966554281,6.241148933107169,-3.270623771541062,-10.0,7.376133320997642,0.2569287508993239,-10.0,1.6751997334569875,-3.9572017121828433,-4.844880701732422,1.7721451493088147,5.2003406985123934,0.8998625304119818,0.9757565430808253,0.5231775043022197,-0.25507598907254325,5.523283334524805,4.703056600211234,-0.11875626022363917,-1.4054408134507548,4.329578022851065,1.2939061977523367,-7.5847527668164085,5.16090348007503,-1.039361582395601,1.5200776262637747,-7.008602538215438,2.3506138213816854,8.535857587994789,-4.953633664439478,-7.97206709514078,-6.280328471819507,2.8116237819517553,0.8540749450864874,0.6377157022761331,-0.9021463287892452,-1.6414016992765728,2.4733604889980936,1.3403544052020204,-6.106097126489438,-2.719469720553491,4.530792293819057,-4.2312664709017715,-9.051745577382503,6.95737081171858,-0.27474771872644915,8.503884152961717,5.152854781851863,7.602222789851348,0.5892702010951698,-4.891034453761941,-4.232061806032782,-1.1574284210608805,8.492466897068255,4.001665773103566,-0.8836170172050368,4.278778230591482,-3.094080020149443,8.31913184036815,7.248807518917257,-9.327191905390524,0.21524797912028315,-2.0087233018374366,-6.582670976447802,-4.178654760756065,1.4973106489698553,-5.2243999731685316,0.4378029248719284,7.951298657688316,-6.525900142495583,-3.7386713884458342,-3.881869130846113,-3.566698173382286,-6.143498568279552,0.11057820610931857,-6.870500226236112,-8.444097152154372,-2.3968047586712222,-1.6268036441424973,5.106017296627328,-7.434731921860306,2.1833976726804885,-5.648852978410369,-7.8519605270297905,0.7233112884941371,-3.333307827235116,0.8923787051853145,4.680701207968462,-0.6267026074029871,-1.8325519311362757,5.43485243328652,6.267685184590469,1.595140343226232,-6.466315461910796,-4.0370135146726716,0.5386674774982789,-8.977945566285861,-10.0,-2.0007832079920314,-1.1676490594624958,4.894589113863869,-5.20177493177313,2.266827841056748,-0.5624075529294126,3.2736283024647292,6.17965564384728,-4.974614660822189,-1.7057785805811254,7.7581110905939745,-1.2645705176510071,1.284127251749646,4.6573217332651105,3.5365234239522216,-0.9426833338382751,5.808663222999608,-3.464612198550816,0.5244342981927046,-6.233849007372674,-6.973024770080239,-1.9043380404135843,-0.6726897400628435,6.822886789972441,5.66878439582644,0.9687337994453346,2.112251906655078,-7.233464792647105,-3.8453535513861876,3.406749702655852,-3.000671847806184,-8.43725346611179,4.738957473806845,-5.675347786247717,-3.112086361013657,-6.9865157740706785,-1.3170554224371323,6.087370565541811,6.9328864992824295,-7.4242518165503615,-1.3074639550957574,3.1851614259460526,-1.0647049149111982,4.178758922770476,1.1077917488344013,-4.844643545455244,-0.9459382572537657,-5.860664473407785,-1.8474992509633694,2.2340076510759017,1.4520047304699188,3.6219720238304665,5.0705092782429695,5.907275974996668,-3.249362573120477});

		HierarchicalController returnToRoom = new HierarchicalController(4,10,3,new int[]{0,1,2,3}, "Return", new double[]{-3.8391056468408498,-1.6936429236446666,3.4178301053585454,1.5256774314805042,-1.2763704918780008,-5.201098603899984,-1.041014160624231,-3.0672861077460944,-1.8029990676127887,-0.419281986424731,-6.083180763688323,6.234076981409077,-7.320708687627166,2.949325578878658,5.364096315611768,2.3818507000370723,4.289472393420975,1.4122984570894932,-4.949466729009384,0.2793282653560337,-1.9142263403950996,3.9134240595371788,-0.36197318825791225,1.6450338876743,5.543963866511765,-3.264067783509966,-1.575498765688296,8.22995802215906,-2.655798775246183,4.142289101049574,3.845906471898977,5.53074433840369,-4.807311356925635,-0.8691409795835906,2.4739960776934833,4.924343186745406,-5.851330462259243,-1.7830231791184425,-3.2219589737838312,4.05952606228125,-2.913507896909381,0.18264199178143653,4.410118773847234,0.5738907844717736,-5.945527760279793,-0.49483185857784084,0.5466766630939325,-0.08349777554602916,-3.3416924189395605,1.8293836273740707,-4.274882519113647,2.529236373498428,10.0,1.9577092420629754,0.021465801380433955,10.0,3.61002319315557,3.214510419668124,5.921771254760381,2.8333906660245893,-1.1676849700939134,1.3807244009705035,2.3971886030797602,-1.8920091095966198,-4.771075815272994,5.080832827120946,7.696563733952038,-3.017654738420217,-1.9429617160355868,-0.44404701867568463,-1.6946007808920052,3.2347387541494594,3.5818957825785587,1.0520082908269632,2.2905887228580672,-1.7863979990320409,-3.5886720717664615,2.4050171490593177,-0.7490064612000681,6.058241710709932,-6.788105771033106,0.41157263751984796,0.6609961231036445,-2.0064163290264445,2.6387131913584425,0.45938839687479704,-3.449405283708221,-0.4101919801130769,5.364678277186242,1.5272760323866605,-4.99265949513712,-0.8963189499930995,-5.170202191369197,-0.738925101154299,2.411839852445344,-4.099802551435617,5.196116984251021,1.4705846609843254,0.580028834902923,-1.8209699517839428,-4.351192107054237,0.7454251959143288,-0.5997444436349779,-10.0,-5.822486336635787,-3.746632794288273,-3.435381495689308,-3.383720720633513,5.835691747370946,4.205463035191803,-1.4568848231612928,-9.031080543099955,2.9469278694861107,3.654201829481347,-1.1244936712088283,4.396211563795415,3.6357400868637564,8.521907423156577,-5.531197817253652,3.8305565090990252,1.6209540168198315,-1.1168593804110003,1.7461360947181377,-7.5368976987668965,-4.219253644034923,-6.698866623342132,0.689409654487697,-8.663269583476321,0.005636962280080704,3.555660572027734,-8.182711968493647,-4.018857092028219,-1.6965343857770592,-3.065948766575232,1.9987615938966132,-5.482237738449284,2.2481791540107325,7.76585283326858,4.703874663802135,-4.406330491982186,-3.698616824927168,-3.127353063183695,3.289801467165116,7.781478762941255,0.1748160430011103,-0.27315144213484166,4.847527425725012,1.829147796146457,-8.302994119187122,-5.420195260398618,-5.586890177295,-2.3259855838199046,-5.641928705879946,5.4297685072828,-4.273448460501051,4.489170882854761,-7.910952280408972,3.5224652500391147,1.4546658190502249,-0.6414964959779217,-1.3458553668187316,-2.566100729374885,-0.29175268056819254,0.8576295666447624,-2.3051203656118933,3.1696523138713277,-6.602026889924945,2.7079934243685657,-7.113763837534204,4.39271219136098,4.347794388325141,4.598732510177168,-0.40343991839185517,-2.5796576853058255,-3.9926072784718034,-2.877492329456123,10.0,1.4266025662020723,0.06974143405825761,0.7130179973161553,2.665979593253523,-1.4149950981190087,2.126462778704367,5.79952090597422,-7.924760038895692,-9.439586000322066,-4.497199931795049,-4.257766725986206,-3.168448230986821,5.573890245973668,0.21457532387348743,-1.5887925889978467,-0.07508810591917547});
		returnToRoom.setSubControllers(new CTRNNMultilayer[]{left,right,corridor});
		
		HierarchicalController maze = new HierarchicalController(6,10,3,new int[]{0,1,2,3,4,5}, "Maze",new double[]{-1.8071714165370405,-5.900749765882461,3.133717421758707,-4.657307946395929,-1.179048825226694,0.2517868707266142,0.7525000520331477,2.0168863899919933,-3.6293309632694792,-4.540096240852271,2.7229060929375914,7.389344529333236,2.509983752492065,1.0866682295085202,-4.705295975196756,-6.550642039744479,6.73463857652224,0.4931306119539353,-2.247771714961664,-1.4321530233037094,3.3135406351278704,-0.5593347149032606,-1.301940916590528,2.86149601928732,4.951484627124352,-6.79022766277659,4.7661408354940615,-1.3147824382825477,-2.7081828362925275,-1.6704327584413037,1.3953041579285212,-0.7114944644513632,-0.7784561884945768,0.2751227874217309,-8.025431949402941,6.215551451795035,-3.486283901679246,-4.015712710402163,2.650558757642679,0.748268028549326,-0.6225062448581282,-0.24668689062773874,9.947247135933127,1.4475519405924557,-0.9989652450478217,1.5551431319275808,3.2682668314758443,-6.295343716983551,0.8334254739731057,1.6852742007673804,0.6039503519799276,0.9521240295292539,-0.7550043609157346,-2.7238172161030785,-2.881519580222575,1.0954122491162397,-1.7424841271652607,-3.6473410030433353,1.4654669797752495,1.83497968350882,-8.175439020701994,-2.2786698817205795,-7.559174452729707,7.21523676018011,-0.8487969387741845,2.7001012960292554,-5.7129007533694045,1.3080513820433561,1.6609969159309348,-5.416655268464748,3.974364868341173,1.0921484112091557,-2.540562099593429,-5.608307906826319,-1.099235613859322,-6.161853968848936,-0.6066961396231833,0.8604734364134461,4.909464791207968,-3.14480471376225,3.927372392899433,6.611995582334487,-5.791162199643897,2.9749184877623187,3.2964656493556395,4.186985642766856,-1.0273452249405008,-1.6456606071603685,-3.783990807056183,-7.0372036828670925,3.577978528385298,4.777566990969233,-2.992869585831657,-2.1633490457284186,3.385112060087481,-0.8578022195447736,2.5575494377450174,-6.41811854494499,0.5595506470956042,-0.05791476062394363,3.7047967575426086,1.708213603744957E-4,-3.9494306578161265,-6.488567117304223,-2.0435837042112195,-4.701942469442242,-6.944084243460745,1.1954220661397557,-3.2775712615895283,1.7350191496808023,6.1943013170817665,0.3126203271541692,-3.906929721533979,2.1780204276310955,-6.737729426300757,3.9092419441809714,-0.06624566409308658,-2.7974605218590267,-2.0618444157490097,1.1188859776099886,4.847173453970908,5.669801882968258,-8.569062087784877,3.8256452426319196,-3.5467501065492906,-2.1797800463043098,-0.44228903387118357,4.374796208538027,7.046233228422294,-1.7825006659711147,-1.6152152047181692,-4.161870442691003,-0.783378236189902,0.10069969627712996,-0.2732084589696535,-4.113370827960142,-1.6732901623025502,-4.389814714951189,-0.6637414201440104,1.9725511446057635,-0.789125786520191,-1.4114997921569175,-8.745143215051217,-10.0,1.2983876565484838,-2.494553055423261,-2.4286565838689027,5.627541531969292,-4.3074792673661895,-1.3848602034055961,1.3284420748192134,-1.1136913981655039,-4.288810830817811,5.463625215232672,3.389215890049893,5.306514958408071,-4.727956080517135,5.9476472952789345,3.1892923035131666,-0.3175126710925199,-3.5358670251837525,-0.5029728995608832,-5.429419948387897,0.9144994657565219,5.321543892356548,-2.546624416103635,0.23428085755030692,-5.431801028189751,-2.4954861866750733,-4.1831164292559535,-2.9595490765611148,-0.6001178100803899,-3.8877368831130976,3.8348085504026486,-6.8764270987931075,4.856159127061391,4.920240852362225,-1.0310381267154785,1.000981833551785,6.399021345069994,-2.821220481278146,2.244785037404928,0.5732559247485631,5.113092022769324,0.10575071846238915,6.008665174086478,-1.8144610187223837,3.153603060521963,-9.318627998643423,-8.425421919322867,1.5300211253582225,-0.9638996880372921,-1.5047209346278918,5.530415323470193,2.650319109266562,-3.6137949103563725,-5.977904510941044,1.3306220353465918,-4.731739162746827,5.906160082564679,-8.397966215458519,3.2626402300003616,5.5637751988319,-2.6025335744633473,-0.8893896029699924,-4.762616797770676,-1.055134103106271,-7.278822424471503,-0.5942854189033399,2.6110242895291287,3.704935788003217,1.9535059482730448,2.928818565868882});
		maze.setSubControllers(new CTRNNMultilayer[]{left,right,corridor});
		
		HierarchicalController main = new HierarchicalController(5,10,3,new int[]{0,1,2,3,6}, "Main",new double[]{9.469652150983297,-7.660111608964832,-10.0,5.72880338192778,-10.0,3.1744194846262697,-6.583768714019147,7.1810919713528145,-3.821822529343142,-10.0,-2.4844487199471006,4.599327828267506,4.2957876450006465,-7.971958385153397,-9.863271959003956,-9.775742913711264,9.995218333458396,8.776913302288428,3.3564517976173223,-3.4932543849120616,-2.885419782072021,-8.894250061744419,6.9362816664066385,1.8063025337081162,8.708741172652152,7.958572556612242,7.502560221524341,-7.342689141452003,9.234302501984693,-2.3734837963763162,6.858990368101733,6.980675515154072,-1.478364288925149,-0.10671643861505178,2.701504712842177,-2.600322374823208,6.303282540220655,-2.678597288270389,-2.9647212287274476,-5.139193756193144,-5.936582833806895,-4.494555123942518,-2.4845167585794297,-0.8884304670444036,-8.443171033621175,-2.85958350905528,-5.64841388561544,-2.0020680614333792,0.3731930576430665,-5.60661825012384,0.5803031729924768,10.0,5.210816429250124,0.1609467815965899,-1.512465487113093,9.800670011687204,-6.612819736093834,3.2726323635444006,-3.425846030037562,-5.953144710294772,3.4939171352978122,-1.2787082442178273,3.5100227132406063,9.338459080443387,-1.0348410798914056,3.0620655374963093,3.1731003792021393,8.818060283648508,0.034703197412366604,2.3921276341546887,10.0,-8.11277258604901,6.044636339812444,-3.8041919806226328,-4.516830355477486,-3.036821436219622,-4.851311957678698,-5.04859968333193,9.952719490649855,6.307401145786137,3.8892700940782943,-1.8122511116546358,-10.0,-2.790379709659121,-4.207749263375807,2.1600701455744087,-5.45492176018274,-2.976135432004474,-5.400422912980214,-1.7343738688782058,0.03965755939833937,9.232814801902421,-5.562066602760357,-4.0014455600173395,2.1357368110607737,-10.0,9.744510694255407,1.9670670497951028,4.091184052253035,6.286260714161085,4.888403284880793,3.8288155600263742,-0.935117131003568,-3.173195479046008,3.865393742212461,-7.820935325255943,-3.2888503173136105,1.9910071329028454,-0.7627122240811498,-7.414330759554953,-0.7862609910031184,9.157369541649338,-7.170353375069641,2.011175687673351,-3.106582741480816,-1.8327102712558694,2.960677483972921,1.1560456699640937,6.885833367512592,1.4007258991388882,10.0,8.486532053529492,-6.861627871002555,-6.181587784340482,3.0038979590768466,-1.4675043416492373,-3.1072612573141285,4.3223351974602515,1.4012764196308152,-10.0,8.361737545972185,-3.010700238627821,8.955339960352235,-2.3921044919389427,0.006623381172568554,7.957569217597675,3.6675742657631796,8.59330961275806,2.9609109019958684,-5.559239199503127,3.8449448888202142,-1.5454982660774568,6.8746749645644805,-8.715885078793033,3.6403900127110087,4.295462273753415,2.1584353592200385,3.2092527423208854,-1.0999698211624573,-4.13179146039653,0.2530509088325703,-1.2297539758854352,8.689622586067657,-1.857512312509165,8.305324093382678,-5.422363160509708,4.851637578383291,5.311314680808505,-7.829043709900038,5.615665359061731,9.962146864600719,0.2922488453585994,-0.9258509363175281,3.3405662725582133,10.0,-5.864919503486057,-4.751464853036936,9.189938510082516,-3.3114711177062452,1.1235288609876197,9.792083245440732,3.6193249260025584,-1.5904157801209293,7.571694681990765,3.7632817173125623,10.0,-1.8234065750289536,-2.2780912394358532,-1.1942244522895233,0.16415020222001409,-3.048188156535927,-4.514700655227248,4.469508539816272,0.20355610366568266,-3.419713399203613,8.345206503565574,-2.8889172189702617,-6.868405919131302,6.728759657378059,-3.4447111887668846,-3.6810417942434306,-2.555144377481425,-2.9903544485600264,4.351100630143437,-2.474593680990764,-1.4354165284751117,5.778568633018683,-9.026503793386759,-3.2745399623580984,2.7530038331428117,-3.074697777303399,9.876584623701897,10.0});
		main.setSubControllers(new CTRNNMultilayer[]{room,maze,returnToRoom});
		
//		Controller controller = new Controller(right,7,2);
		Controller controller = new Controller(new BluetoothHandler(),enterCorridor,4,2);
		ControllerFrame frame = new ControllerFrame(controller);
		
		controller.setFrame(frame);
		controller.start();
	}
}