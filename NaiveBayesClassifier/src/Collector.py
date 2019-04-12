from Parser import Parser
import os
import re
from DataModel import DataModel
import math

class Collector:
    delta=0.5
    def __init__(self):
        self.parser=Parser()
        self.hamWordList=[]
        self.spamWordList=[]
        self.wordList=[]
        self.wordDict={}
        self.hamDict={}
        self.spamDict={}
        self.modelDict={}
        self.lineList=[]
        self.vocSize=0
        self.hamSize=0
        self.spamSize=0
        self.classDict={}
        self.stopWord=[]
        self.removeWordLength=[0,10000000]
        self.isRight=0
        self.isWrong=0
        self.hamRemoveSize=0
        self.spamRemoveSize=0
    def dataCollector(self,pathName):
        path=pathName
        fileNames=os.listdir(path)
        
        hamWordList=[]
        spamWordList=[]
        
        for file in fileNames:            
            fileName=os.path.join(path,file)
            f=open(fileName,mode='r',encoding='latin-1')
            isHam = (file.find('ham')!=-1)
            
            
            if isHam:
                self.hamSize+=1
            else:
                self.spamSize+=1
            
            lines = f.readlines()
            for oneLine in lines:
                line = oneLine.lower().strip()
                if line=='':
                    continue
                list=re.split('[^a-zA-Z]+',line)
                for i in range(len(list)-1,-1,-1):
                    word=list[i]
                    if word=='' or word in self.stopWord:
                        list.remove(word) 
                        continue
                    if len(word)<=self.removeWordLength[0] or len(word)>=self.removeWordLength[1]:
                        list.remove(word)
                        continue
                    if word not in self.wordList:
                        self.vocSize+=1
                    if not word in self.wordDict:
                        self.wordDict[word]=1
                    else:
                        self.wordDict[word]+=1
                
                if isHam: 
                                       
                    hamWordList+=list
                else:    
                    spamWordList+=list                                
            f.close()
            

        print("begin add list")
        self.wordList+=hamWordList+spamWordList
                                 
        self.hamWordList+=hamWordList
        
        self.spamWordList+=spamWordList
#         print("end add list")
#         self.writeFile(hamWordList+spamWordList) #write list to file to analysis, has many duplicated word
    def infrequentProcess(self,command):
        
        
        count=0
        removeIndex=0
        
        if command>=1:
            
            for key,value in sorted(self.wordDict.items(),key=lambda w:w[1]):
                if value>command:
                    break
                if self.wordDict.__contains__(key):
                    self.wordDict.pop(key)
                
        if command<1:
            removeIndex=int(float(command)*len(self.wordDict))
            
            for key,value in sorted(self.wordDict.items(),key=lambda w:w[1],reverse=True)[:removeIndex+1]:
                
                if self.wordDict.__contains__(key):
                    self.wordDict.pop(key)
                    
            
#         for i in range(len(self.wordList)-1,-1,-1):
#             word=self.wordList[i]
#             if word in removeList:
#                 self.wordList.remove(word)
             
            
    def writeFile(self,fileWordList):
        name='xr_list.txt'
        base_dir=os.getcwd()
        file_name=os.path.join(base_dir,name)
        
        file_open=open(file_name,'a')
        for line in fileWordList:
            file_open.write(str(line)+'\n')
        file_open.write('\n')
        file_open.close()      
        print(name+' : word list file has saved!') 
             
    def buildModel(self,pathName,fileName):
        self.vocSize=len(self.wordDict)
        
        for word in self.hamWordList:
            if not self.wordDict.__contains__(word):
                self.hamRemoveSize+=1
            if self.hamDict.__contains__(word):
                self.hamDict[word]+=1
            else:
                self.hamDict[word]=1
                
        for word in self.spamWordList:
            if not self.wordDict.__contains__(word):
                self.spamRemoveSize+=1
            if self.spamDict.__contains__(word):
                self.spamDict[word]+=1
            else:
                self.spamDict[word]=1
        if self.hamRemoveSize!=0:
            hamCounts=len(self.hamWordList)-self.hamRemoveSize
        else:
            hamCounts=len(self.hamWordList)
        if self.spamRemoveSize!=0:
            spamCounts=len(self.spamWordList)-self.spamRemoveSize
        else:
            spamCounts=len(self.spamWordList)
       
        count=1
        for word in self.wordDict:
            if self.modelDict.__contains__(word):
                continue
            lineNum=count
            curWord=word
            hamFrenquency=0
            if self.hamDict.__contains__(word):
                hamFrenquency=self.hamDict.get(word)
            conHamPro=(hamFrenquency+self.delta)/(hamCounts+self.delta*self.vocSize)
            spamFrenquency=0
            if self.spamDict.__contains__(word):
                spamFrenquency=self.spamDict.get(word)
            conSpamPro=(spamFrenquency+self.delta)/(spamCounts+self.delta*self.vocSize)
            curModel=DataModel(lineNum,curWord,hamFrenquency,conHamPro,spamFrenquency,conSpamPro)
            self.modelDict[word]=curModel
            count+=1 
        path=pathName 
        name=fileName
        fileName=os.path.join(path,name) 
        f=open(fileName,'w',encoding='latin-1',errors='ignore') 
                       
        for model in self.modelDict.values():
            line=self.parser.modelParser(model)
            f.write(line+'\n')
        f.close()
        
    def doClassify(self,testName,resultPath,resultName):
        path=testName
        fileNames=os.listdir(path)
        count=1
        realHamAndRight=0
        realSpamAndRight=0
        realHamAndWrong=0
        realSpamAndWrong=0
        
        for file in fileNames:
            if self.classDict.__contains__(file):
                continue
            fileName=os.path.join(path,file)
            f=open(fileName,'r',encoding='latin-1',errors='ignore')
            hamScore=self.hamSize/(self.hamSize+self.spamSize)
            spamScore=self.spamSize/(self.hamSize+self.spamSize)
            logHam=math.log10(hamScore)
            logSpam=math.log10(spamScore)
            
            while True:
                line=f.readline()
                if line=='':
                    break
                list=re.split('[^a-zA-Z]',line.lower())
                for word in list:
                    
                    if word=='':
                         
                        continue
                    if self.modelDict.__contains__(word):
                        data=self.modelDict[word]
                        hamScore*=data.conHamPro
                        spamScore*=data.conSpamPro
                        
                        logHam+=math.log10(data.conHamPro)
                        logSpam+=math.log10(data.conSpamPro)
            
            classify='ham'
            if logSpam>logHam:
                classify='spam'
            correction='right'
            realClassify=classify 
            if file.find(classify)==-1:
                correction='wrong' 
                if realClassify=='ham':
                    realClassify='spam'
                else:
                    realClassify='ham'
            if correction=='right':
                self.isRight+=1
            else:
                self.isWrong+=1
            dataStr=str(count)+'  '+file+'  '+classify+'  '+str(logHam)+'  '+str(logSpam)+'  '+realClassify+'  '+correction
            self.classDict[file]=dataStr  
            count+=1
            if classify=='ham' and realClassify=='ham':
                realHamAndRight+=1
            elif classify=='ham' and realClassify=='spam':
                realSpamAndWrong+=1    
            elif classify=='spam' and realClassify=='spam':
                realSpamAndRight+=1
            else:
                realHamAndWrong+=1
            f.close()
        path=resultPath 
        name=resultName
        
        fileName=os.path.join(path,name) 
        
        f=open(fileName,'w',encoding='latin-1',errors='ignore')                
        for dataClass in self.classDict.values():
            
            f.write(dataClass+'\n')
        f.close()
        print('right classify: '+str(self.isRight))
        print('wrong classify: '+str(self.isWrong))
        print('real ham and classify ham: '+str(realHamAndRight))
        print('real ham and classify spam: '+str(realHamAndWrong))
        print('real spam and classify spam: '+str(realSpamAndRight))
        print('real spam and classify ham: '+str(realSpamAndWrong))