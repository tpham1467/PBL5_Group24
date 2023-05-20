from gtts import gTTS
import playsound

text = "Bạn đã gọi 1 gà quay  1 bún bò ."
output = gTTS(text,lang="vi")
output.save("output.mp3")
playsound.playsound('output.mp3', True)