package com.example.maxim.shortstories2;

import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class WallVk {
    private String name;

    @Override
    public String toString() {
        return this.name;
    }

    public WallVk(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        String response = "";
        switch (name) {
            case "Подслушано":
                response = "{\"response\":{\"wall\":[68134,{\"id\":887996,\"from_id\":-34215577,\"to_id\":-34215577,\"date\":1479655802,\"post_type\":\"post\",\"text\":\"Дочка спрашивает: \\\"Мама, можно я полотенчико кухонное возьму кое-что протереть?\\\" Говорю, возьми. Через полчаса: \\\"Мама, у меня тебе сюрприз, я тебе сапожки на завтра протерла, завтра в чистых пойдёшь\\\". Как тут ругать? Человек от души сюрприз готовил, приятно.<br><br>   #Подслушано_детство@overhear\",\"comments\":{\"count\":0},\"likes\":{\"count\":29992},\"reposts\":{\"count\":130}},{\"id\":887980,\"from_id\":-34215577,\"to_id\":-34215577,\"date\":1479651914,\"post_type\":\"post\",\"text\":\"Если вы в данный момент испытываете непреодолимое желание общения, ставьте лайк напротив своего города или под этой записью! <br> <br>Далее выбирайте людей из тех, кто поставил лайк и общайтесь! Не стесняйтесь писать первыми, никто вас не осудит, ведь вы из \\\"Подслушано\\\" .)\",\"attachment\":{\"type\":\"photo\",\"photo\":{\"pid\":456239535,\"aid\":-7,\"owner_id\":-34215577,\"user_id\":100,\"src\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d970\\/GGmC6bk-Vyc.jpg\",\"src_big\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d971\\/3N1Elby5sUI.jpg\",\"src_small\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d96f\\/XkzOTYbDKxw.jpg\",\"src_xbig\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d972\\/ceHG9gJvpiE.jpg\",\"src_xxbig\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d973\\/LoLo4X4u33c.jpg\",\"width\":1280,\"height\":960,\"text\":\"\",\"created\":1479644862,\"post_id\":887980,\"access_key\":\"66a14a7870ff6de352\"}},\"attachments\":[{\"type\":\"photo\",\"photo\":{\"pid\":456239535,\"aid\":-7,\"owner_id\":-34215577,\"user_id\":100,\"src\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d970\\/GGmC6bk-Vyc.jpg\",\"src_big\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d971\\/3N1Elby5sUI.jpg\",\"src_small\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d96f\\/XkzOTYbDKxw.jpg\",\"src_xbig\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d972\\/ceHG9gJvpiE.jpg\",\"src_xxbig\":\"https:\\/\\/pp.vk.me\\/c836629\\/v836629366\\/d973\\/LoLo4X4u33c.jpg\",\"width\":1280,\"height\":960,\"text\":\"\",\"created\":1479644862,\"post_id\":887980,\"access_key\":\"66a14a7870ff6de352\"}}],\"comments\":{\"count\":0},\"likes\":{\"count\":7849},\"reposts\":{\"count\":2}},{\"id\":887977,\"from_id\":-34215577,\"to_id\":-34215577,\"date\":1479651301,\"post_type\":\"post\",\"text\":\"Собиралась на работу, рассказывала парню про коллектив, то-сё. Подходит и молча в полупопие кусает сильно так. На мой вопросительный вопль объяснил: \\\"Чтобы знали, что ты не свободна!\\\" На мой логичный вопрос, как часто мне надо раздеваться, чтобы показать это назойливым чувакам - обиделся. На следующий день пришел с кольцом. Но первый вариант тоже норм был, чё :)<br><br>   #Подслушано_странное@overhear\",\"comments\":{\"count\":0},\"likes\":{\"count\":27336},\"reposts\":{\"count\":117}}],\"profiles\":[],\"groups\":[{\"gid\":34215577,\"name\":\"Подслушано\",\"screen_name\":\"overhear\",\"is_closed\":0,\"type\":\"page\",\"photo\":\"https:\\/\\/pp.vk.me\\/c636125\\/v636125366\\/30e37\\/iPhN611dcbY.jpg\",\"photo_medium\":\"https:\\/\\/pp.vk.me\\/c636125\\/v636125366\\/30e36\\/IlmaxBAbJf4.jpg\",\"photo_big\":\"https:\\/\\/pp.vk.me\\/c636125\\/v636125366\\/30e35\\/t0B0t4_e9PY.jpg\"}]}}";
                break;
            case "Just Story":
                response = "{\"response\":{\"wall\":[3746,{\"id\":19623,\"from_id\":-106084026,\"to_id\":-106084026,\"date\":1479416827,\"post_type\":\"post\",\"text\":\"Вот смотришь фильмы и там герои, у которых есть способность к телекинезу, ломают дома и швыряются машинами в противника. <br>Спрашивается - нафига? <br>Делов-то. Ткнул в глаз мысленным усилием. Или на нерв зубной надавил ментально. <br>Или вообще перемешал мозг противнику...\",\"comments\":{\"count\":0},\"likes\":{\"count\":17007},\"reposts\":{\"count\":452}},{\"id\":19621,\"from_id\":-106084026,\"to_id\":-106084026,\"date\":1479414782,\"post_type\":\"post\",\"text\":\"Наш офис находился на севере Москвы. Типичная ситуация - из-за задранной арендной платы пришлось съезжать. Работал у нас в отделе по развитию бизнеса молодой парень Евгений. Руководство ему доверяло и поэтому именно Евгению поручили за 2 недели найти новое помещение под офис. Пока мы \\\"паковали чемоданы\\\" - Женя искал. И вот через неделю он говорит, что нашел помещение и, мол, поехали смотреть. <br>Два сортира М\\/Ж, душевая комната, просторные кабинеты с кондеями, столовая\\/кухня, зона отдыха с телеком и 2-мя диванами. Не офис, а мечта и цена адекватная, но находился совершенно в противоположной части города. <br>Ездить было сотрудником далековато, т.к. большинство жили в другой части Москвы, но офис был реально огонь! В общем переехали. <br> <br>Как позже выяснилось,помещение находилось в 150 м от дома Евгения. <br>Хитро-хитро, Женя.\",\"comments\":{\"count\":0},\"likes\":{\"count\":27501},\"reposts\":{\"count\":282}},{\"id\":19619,\"from_id\":-106084026,\"to_id\":-106084026,\"date\":1479413762,\"post_type\":\"post\",\"text\":\"Из бурной геолого-геофизической молодости: <br> <br>Начальник сейсмопартии поздно ночью привёз в партию зарплату. Банковских карточек в те времена не было, деньги выдавали живьём. Сумма с полевыми на двести чел немаленькая. Ввиду позднего времени раздачу слонов назначили на утро. <br>Под утро начальника разбудили известием, что сейф спёрли. Начальник как был в неглиже, опрокидывая мебель и сбивая с ног зазевавшихся сотрудников, босиком по снегу рванул к офисному вагончику. Металлическая дверь вагончика была вырвана «с мясом», сейфа не был. Начальник бросился к тумбочке, на которой прежде стоял сейф, распахнул дверцы – деньги мирно лежали на полочке целёхоньки – и рухнул на стул, смахивая холодный пот со лба. <br>Устав после дороги, начальник поленился вечером возиться с сейфом и просто забросил деньги в тумбочку. <br> <br>Пару дней спустя раскуроченный сейф нашли в нескольких километрах от базы партии.\",\"comments\":{\"count\":0},\"likes\":{\"count\":19524},\"reposts\":{\"count\":280}}],\"profiles\":[],\"groups\":[{\"gid\":106084026,\"name\":\"Just Story\",\"screen_name\":\"just_str\",\"is_closed\":0,\"type\":\"page\",\"photo\":\"https:\\/\\/pp.vk.me\\/c630128\\/v630128525\\/3537b\\/EukmJq5Ey8s.jpg\",\"photo_medium\":\"https:\\/\\/pp.vk.me\\/c630128\\/v630128525\\/3537a\\/ImCXJUbJdGc.jpg\",\"photo_big\":\"https:\\/\\/pp.vk.me\\/c630128\\/v630128525\\/35379\\/U3pk4LbEsIM.jpg\"}]}}";
                break;
            case "New Story":
                response = "{\"response\":{\"wall\":[1869,{\"id\":84093,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480320194,\"post_type\":\"post\",\"text\":\"Ходили с мамой как-то по магазинам в ТЦ, я воодушевленно рассказываю историю, которая приключилась со мной на работе, с полной уверенностью, что она меня слушает, но моя ненаглядная остановилась у витрины, а я пошла дальше, и, когда я заподозрила неладное молчание в ответ, обернувшись, увидела мужчину, лет 50 с вопросом: \\\"Ну? А дальше что?\\\"\",\"comments\":{\"count\":3},\"likes\":{\"count\":79},\"reposts\":{\"count\":2}},{\"id\":84066,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480316655,\"post_type\":\"post\",\"text\":\"Моя соседка замужем, у нее есть любовник электрик, этот электрик муж парикмахерши из нашего дома. У этой парикмахерши есть любовник не бедный, который, в свою очередь, трахает еще одну молодую деваху из соседнего дома. Та, в свою очередь, встречается с парнем, который служит в армии и якобы его ждёт, у этого парня есть сестра, которая замужем за начальником каким-то хамоватым, который... <br>Баба Шура со 2 этажа - главный эксперт на районе. Просто забудь ключи и присядь на лавочку.\",\"comments\":{\"count\":10},\"likes\":{\"count\":500},\"reposts\":{\"count\":8}},{\"id\":84041,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480313041,\"post_type\":\"post\",\"text\":\"Троллейбус. Народу немного. На переднем сиденье о чём-то тихонько щебечут бабушка с внуком лет трёх. Мальчишка – ангелок: огромные голубые глаза, длинные загнутые ресницы, золотистые кудряшки. Тут входит мадам из тех, которым всё надо, и начинает приставать к ребёнку: как зовут, ах какой красивый мальчик, какие глазки, какие волосики, как тебя бабушка хорошо воспитала... Малой начал вжиматься в бабушку, а потом громко так, на весь троллейбус, спрашивает: «Бабулечка, можно я скажу на тётю \\\"блядь\\\"?»\",\"comments\":{\"count\":11},\"likes\":{\"count\":624},\"reposts\":{\"count\":11}},{\"id\":84006,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480309405,\"post_type\":\"post\",\"text\":\"В прошлом году поехала на море, влюбилась на полном серьезе в матроса, зацепила его и переспала с ним. На этом бы все и кончилось. Приехала домой - дома муж и супружеский долг. <br>Спустя время обнаружилась гонорея. Я в ужасе: заразила мужа от матросни! Мужу все рассказала, он психанул и ушел из дома. Его можно понять. Через день позвонил матрос, спрашивал, как дела, то да сё. Наорала на него, мол, ах ты, не предупредил меня, что у тебя триппер! Он в отказ: какой триппер, ты что, откуда? Раз в два месяца по врачам, да и от триппера вонь какая, ты разве от меня ее чуяла? Я разозлилась и бросила трубку. Обидела его. Начала лечиться. <br>Через полгода узнала, что триппер принес муж от девки с работы. Этим летом вышла замуж за своего матроса. Муж женился на коллеге. Пожрали салатики друг у друга на свадьбе. Нет худа без добра)\",\"comments\":{\"count\":28},\"likes\":{\"count\":790},\"reposts\":{\"count\":8}},{\"id\":83982,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480305797,\"post_type\":\"post\",\"text\":\"Разговаривали с другом о будущем, он признался, что в растерянности: не знает, чем заниматься дальше. Мне всегда было проблематично находить нужные слова для поддержки. Вижу - фонарь горит на улице. Меня это натолкнуло на мысль, я так смачно затягиваясь, с довольной улыбкой (нужные слова найдены) начинаю свою речь: \\\"Видишь, фонарь горит? Представь, что это - твоё светлое будущее....\\\" И во время этого - фонарь гаснет. Тишина. Друг говорит: \\\"Ну хули, спасибо\\\". Так мой дебют великого оратора не состоялся.\",\"comments\":{\"count\":12},\"likes\":{\"count\":956},\"reposts\":{\"count\":33}},{\"id\":83971,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480302196,\"post_type\":\"post\",\"text\":\"Шла вечером с фото-сессии с питоном за пазухой. Мой ползучий друг всё норовил высунуться, но я прикрывала его морду шарфиком, заталкивая поглубже в куртку. В парадной налетел мужик, не думала, что когда-нибудь буду рада подобному, но в этот раз просто звёзды сложились. Я даже не сопротивлялась, когда он рванул молнию куртки (признаю, растерялась). Змей же почуяв свободу радостно вывалился на обидчика. Боже, как визжал этот мужик, как он удирая, споткнулся и летел с лестницы! (Со змеем всё ок)\",\"comments\":{\"count\":19},\"likes\":{\"count\":1074},\"reposts\":{\"count\":11}},{\"id\":83893,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480284184,\"post_type\":\"post\",\"text\":\"В детстве у моего младшего брата было много водных автоматов и пистолетов. Так вот, каждое лето у нас была традиция обстреливать водой бельё, которое сушили на балконе соседи. Бабка-соседка офигевала, почему в сорокоградусную жару бельё не сохнет, и это сводило ее с ума. А мы каждый час старательно мочили его из автоматов.\",\"comments\":{\"count\":18},\"likes\":{\"count\":743},\"reposts\":{\"count\":5}},{\"id\":83832,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480280590,\"post_type\":\"post\",\"text\":\"С 13 лет живу в совершенно чужой семье, очень соскучился по родным, впадаю в депрессию каждый ноябрь так как в декабре потерял их, новая семья меня не обижает, даже наоборот, заботятся как о родном. <br>Жаль сверстников, которые обижаются\\/ссорятся со своими родителями по пустякам, желают побыстрее съехать и жить отдельно. Я бы все отдал чтобы хотя бы месяц пожить с родителями, посмотреть телевизор вечером всей семьёй, а потом пойти втроём гулять с собакой.\",\"comments\":{\"count\":49},\"likes\":{\"count\":947},\"reposts\":{\"count\":2}},{\"id\":83778,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480276989,\"post_type\":\"post\",\"text\":\"Сверхнаглость с моей стороны, как со стороны медсестры говорить это, но пациенты дают чересчур много шоколада! Люди, благодарите нас, пожалуйста, колбаской с хлебушком, огурчиками солёными) Мы не успеваем сходить иногда в буфет за сосисками, а на шоколаде не уедешь далеко))\",\"comments\":{\"count\":24},\"likes\":{\"count\":600},\"reposts\":{\"count\":6}},{\"id\":83725,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480273397,\"post_type\":\"post\",\"text\":\"Мне 30. Ушла с ребёнком от очень культурного, красивого и интеллигентного мужа, который не мог ни комплимент сделать, ни похвалить за что-то, ни извиниться, если не прав (я симпатичная и ухоженная, тоненькая). Секса почти не было. Так как не хочу новых отношений пока, а только работать и заниматься ребёнком,\\\"взяла\\\" в любовники друга, которого всегда считала простоватым. Он меня зацеловывает, носит на руках, сюсюкает, всегда готов выслушать и помочь. Я счастлива. Видимо простое оно, женское счастье.\",\"comments\":{\"count\":29},\"likes\":{\"count\":1000},\"reposts\":{\"count\":1}},{\"id\":83693,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480269823,\"post_type\":\"post\",\"text\":\"У моей мамы есть младший брат сорока лет. Что он, что жена - оба лютые бездельники, часто сидят без работы, влезают в долги, занимают у нас деньги без возврата. Есть у этих раздолбаев сын 12-ти лет, особо не видел вкусностей. Мальчик старается заработать, где может. <br>Недавно убрал двор соседа, с которым у его отца плохие отношения. За свой труд получил 150 рублей, потратил на продукты всей семье! В благодарность его папаша наорал и сказал, чтоб не смел больше туда ходить. Боюсь, что станет таким же, как эти амебы...\",\"comments\":{\"count\":28},\"likes\":{\"count\":1098},\"reposts\":{\"count\":5}},{\"id\":83652,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480266207,\"post_type\":\"post\",\"text\":\"Было дело в 90-х. Строила как-то бригада котельную, с высокой кирпичной трубой. В конце работ их кинули на деньги. Они собрали монатки, да уехали. Запускают котельную после этого, она вся в дыму, тяги нет. Все перепробовали, и в трубу заглядывали - небо видать, а тяги нет. В итоге вызвонили бригадира, договорились, что заплатят оставшееся, а он им тягу отремонтирует. Приехал он один, без бригады, пересчитал деньги, достал из багажника ружье и выстрелил в трубу. Наверху было замуровано стекло. Небо видно, а тяги нет.\",\"comments\":{\"count\":19},\"likes\":{\"count\":1222},\"reposts\":{\"count\":44}},{\"id\":83612,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480262619,\"post_type\":\"post\",\"text\":\"Переехали в новую квартиру, и вот только сейчас, пять лет спустя, я понял как нам повезло. Все соседи в основном - молодые пары без детей, или с детьми старше 5-ти лет. <br>Всегда все чисто, надписей никаких нет в подъезде. <br>И даже у подъезда бабушки не сидят и не чернят кого попало: скамейки нет. <br>Да и бабушка всего одна на весь подъезд, и та божий одуванчик. <br>Просто горд за свой подъезд.\",\"comments\":{\"count\":24},\"likes\":{\"count\":900},\"reposts\":{\"count\":1}},{\"id\":83587,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480260784,\"post_type\":\"post\",\"text\":\"Я и муж в превосходстве владеем французским и немецким языками. Если мы ссоримся, то всегда на немецком, а миримся всегда на французском (акценты языков дополняют атмосферу). А на русском у нас просто все всегда хорошо.)\",\"comments\":{\"count\":22},\"likes\":{\"count\":1004},\"reposts\":{\"count\":3}},{\"id\":83576,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480259016,\"post_type\":\"post\",\"text\":\"Снимая обои в комнате, мы обнаружили под ними настенную живопись: Дед Мороз и ёлочка с красной звездой на макушке. Судя по надписи на шапке у дедушки, первые жильцы этой квартиры встречали новый 1976 год с голыми стенами и глобальными планами на ремонт. Нарисуем в компанию к Морозу Снегурочку и подпишем 2017 годом. Ведь с внучкой всяко веселей! Быть может, лет через 40-50 кто-нибудь тоже наткнётся на такой вот привет из прошлого и получит заряд новогоднего настроения, несмотря на непогоду за окном.\",\"comments\":{\"count\":15},\"likes\":{\"count\":837},\"reposts\":{\"count\":5}},{\"id\":83528,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480255403,\"post_type\":\"post\",\"text\":\"Ликвидация склада парфюмерии! <br>✔ Скидка до 85% <br>✔ Работаем без предоплаты, с доставкой по России <br>✔ Курьерская доставка по Москве <br>✔ При покупке 2х товаров подарок от магазина <br>✔ Тысячи довольных покупателей <br>☑Наш магазин https:\\/\\/vk.cc\\/5T4XkY \uD83D\uDC48\",\"attachment\":{\"type\":\"photo\",\"photo\":{\"pid\":456239629,\"aid\":-7,\"owner_id\":-127509226,\"user_id\":100,\"src\":\"https:\\/\\/pp.vk.me\\/c626522\\/v626522063\\/39a5f\\/RwOc-2xUfK8.jpg\",\"src_big\":\"https:\\/\\/pp.vk.me\\/c626522\\/v626522063\\/39a60\\/1aw2HAeOUzQ.jpg\",\"src_small\":\"https:\\/\\/pp.vk.me\\/c626522\\/v626522063\\/39a5e\\/xAOvsXhsvvY.jpg\",\"width\":604,\"height\":541,\"text\":\"\",\"created\":1480172877,\"post_id\":83528,\"access_key\":\"aacb14a1230bf556cf\"}},\"attachments\":[{\"type\":\"photo\",\"photo\":{\"pid\":456239629,\"aid\":-7,\"owner_id\":-127509226,\"user_id\":100,\"src\":\"https:\\/\\/pp.vk.me\\/c626522\\/v626522063\\/39a5f\\/RwOc-2xUfK8.jpg\",\"src_big\":\"https:\\/\\/pp.vk.me\\/c626522\\/v626522063\\/39a60\\/1aw2HAeOUzQ.jpg\",\"src_small\":\"https:\\/\\/pp.vk.me\\/c626522\\/v626522063\\/39a5e\\/xAOvsXhsvvY.jpg\",\"width\":604,\"height\":541,\"text\":\"\",\"created\":1480172877,\"post_id\":83528,\"access_key\":\"aacb14a1230bf556cf\"}}],\"comments\":{\"count\":0},\"likes\":{\"count\":13},\"reposts\":{\"count\":0}},{\"id\":83467,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480251813,\"post_type\":\"post\",\"text\":\"Когда мне было лет семь, стали появляться первые телефоны с видеокамерами. Моя мама купила себе такой. И каждый день перед школой, я ставил стул, на него книги (все это стояло перед телевизором) и ставил мамин телефон, чтобы записать мультики. Зато, когда мы ходил с мамой в больничку, мне было чем себя занять, ожидая очереди. Я смотрел мультики, черт подери, на телефоне. Другие дети подбегали ко мне, услышав знакомую мелодию мультфильма, и спрашивали, как я это сделал. В тот момент я чувствовал себя гением.)\",\"comments\":{\"count\":28},\"likes\":{\"count\":1301},\"reposts\":{\"count\":5}},{\"id\":83407,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480248209,\"post_type\":\"post\",\"text\":\"Мы привыкли видеть любовниц \\\"плохими\\\". Соблазнила, увела, разбила семью, меркантильная сучка! А что если на самом деле там робкий человек, который всеми силами отговаривал мужчину любить её, а тем более уходить от жены. Что если там ангел с прекрасным внутренним миром и тонкой душой? Угораздило меня познакомиться с такой. Планировала космы повыдирать, в итоге расплакались обе, а она попросила прощения. Она лучше меня. Муж искренне ее полюбил. Я отпустила. И винить некого.\",\"comments\":{\"count\":100},\"likes\":{\"count\":768},\"reposts\":{\"count\":5}},{\"id\":83391,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480246385,\"post_type\":\"post\",\"text\":\"Лето. На большом городском празднике, который я помогала проводить, забыли выпустить 200 неоновых шаров, наполненных гелием... Мне их отдали. И мечта сбылась! Ночью под дождём я на велосипеде, а к рюкзаку привязана куча шаров. Все автомобилисты оценили :)\",\"comments\":{\"count\":13},\"likes\":{\"count\":1135},\"reposts\":{\"count\":9}},{\"id\":83374,\"from_id\":-127509226,\"to_id\":-127509226,\"date\":1480242786,\"post_type\":\"copy\",\"text\":\"Дело было летом. <br>Сижу спокойно перед компом, читаю посты. С кухни раздается голос отца: \\\"Дочь, ты у меня такая хрупкая и ранимая. Принеси мои инструменты из машины, окей?\\\" Ну и я такая \\\"Без базара, батя\\\". Надеваю домашние тапочки, ибо до машины только через двор и дорогу пройти, старый плащ. Выхожу на улицу. Жарко. Солнце печет сильно. Натянула капюшон. Лица не видно. Подхожу к машине. Открываю багажник. И что я вижу? Правильно, папину бензопилу \\\"Дружба\\\". Ну что, раз обещала, надо тащить. Подхожу к углу дома, за ним кто-то орет и матерится. Захожу за угол. Вижу компанию гопоты человек 15 и парень с девушкой, прижатые к стенке. <br>Только через пять минут до меня доперло, почему все с криками разбежались в разные стороны. <br>Гопота спокойно делает свои дела. И тут - СЮРПРАЙЗ, МАЗАФАКА! Из-за угла выхожу я, медленным пафосным шагом. Черный плащ до голени, капюшон закрывает лицо, домашние тапочки, бензопила. <br>Я еще в жизни не видела, чтоб кто-то ТАК БЫСТРО бегал!\",\"copy_post_date\":1479824044,\"copy_post_type\":\"post\",\"copy_owner_id\":-133180305,\"copy_post_id\":1058,\"copy_text\":\"[club133180305|Паблик, от которого невозможно оторваться. Подпишись!]<br><br>https:\\/\\/vk.com\\/photo-127509226_456239653\",\"comments\":{\"count\":3},\"likes\":{\"count\":489},\"reposts\":{\"count\":12}}],\"profiles\":[],\"groups\":[{\"gid\":127509226,\"name\":\"New Story\",\"screen_name\":\"nnewstory\",\"is_closed\":0,\"type\":\"page\",\"photo\":\"https:\\/\\/pp.vk.me\\/c629630\\/v629630063\\/3782d\\/E0YYD9-Z0vc.jpg\",\"photo_medium\":\"https:\\/\\/pp.vk.me\\/c629630\\/v629630063\\/3782c\\/ETEHVbQkCXE.jpg\",\"photo_big\":\"https:\\/\\/pp.vk.me\\/c629630\\/v629630063\\/3782b\\/lHg9pQwPMaM.jpg\"},{\"gid\":133180305,\"name\":\"СТЫД\",\"screen_name\":\"pozor\",\"is_closed\":0,\"type\":\"page\",\"photo\":\"https:\\/\\/pp.vk.me\\/c638817\\/v638817828\\/dd1f\\/NQDmipZmIbs.jpg\",\"photo_medium\":\"https:\\/\\/pp.vk.me\\/c638817\\/v638817828\\/dd1e\\/SoC4S3Azdo8.jpg\",\"photo_big\":\"https:\\/\\/pp.vk.me\\/c638817\\/v638817828\\/dd1c\\/Zsi5hjHFQ8k.jpg\"}]}}";
        }
        try {
            return parseJsonResponse(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Post>  parseJsonResponse(JSONObject jsonObject) {
        ArrayList<Post> res = new ArrayList<>();
        try {
            if(jsonObject.has("response")) {
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray jsonArray = response.getJSONArray("wall");

                String wallName = "";
                JSONArray groups = response.getJSONArray("groups");
                if (groups.length() > 0) {
                    wallName = groups.getJSONObject(0).get("name").toString();
                }

                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject curJsonObject = jsonArray.getJSONObject(i);
                    if (curJsonObject.has("text")) {
                        Post post = new Post();
                        post.text = (Html.fromHtml(curJsonObject.get("text").toString())).toString();
                        post.date = Long.parseLong(curJsonObject.get("date").toString());
                        post.wall = wallName;
                        res.add(post);
                    }
                }
                Log.d("tag", String.valueOf(jsonArray.length()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return o.toString().equals(this.name);
    }
}
