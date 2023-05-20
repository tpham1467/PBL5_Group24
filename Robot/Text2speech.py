# -*- coding: utf-8 -*-

def Text2Speech(text):

    from gtts import gTTS
    import playsound

    from googletrans import Translator
    translator = Translator(service_urls=['translate.googleapis.com'])
    translation = translator.translate(text,src='vi', dest='en')

    output = gTTS(text,lang="en",lang_check=True)
    output.save("output.mp3")
    playsound.playsound('output.mp3', True)

Text2Speech('Chào em')