import re
from DataModel import DataModel
class Parser:
    def __init__(self):
        self.wordsList=[]
    

    def modelParser(self,model):
        return str(model.lineNum)+'  '+str(model.word)+'  '+str(model.hamFrenquency)+'  '+str(model.conHamPro)+'  '+str(model.spamFrenquency)+'  '+str(model.conSpamPro)