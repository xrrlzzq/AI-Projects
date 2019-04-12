from Collector import Collector
import os

class DataExperiment:
    
    
        
        
    def doExperiment(self,index):

        collector=Collector()
        path=os.getcwd()
        trainPath=os.path.join(path,'train')
        testPath=os.path.join(path,'test')
        
        
       
           
        if index==2:
            print('you choose stopWord experiment')
            print('please wait...')
            name='stopWord.txt'
            fileName=os.path.join(path,name) 
            f=open(fileName,'r',encoding='gb18030') 
                           
            line=f.read()
            f.close()
            collector.stopWord+=line.split('\n')
            
            resultPath=os.path.join(path,'stopWord')
            modelName='demo-model-exp2.txt'
            resultName='demo-result-exp2.txt'
            self.doExpClassify(trainPath, testPath, resultPath, collector, modelName, resultName)
        elif index==3:
            print('you choose word length experiment')
            print('please wait...')
            collector.removeWordLength[0]=2
            collector.removeWordLength[1]=9
            resultPath=os.path.join(path,'wordLength')
            modelName='demo-model-exp3.txt'
            resultName='demo-result-exp3.txt'
            self.doExpClassify(trainPath, testPath, resultPath, collector, modelName, resultName)
        elif index==4:
            print('you choose infrequent experiment')
            choose=input('please choose value(0) or percentage(1),default is value')
            if choose=='1':
                i=input('please enter the percentage(%)(5%-25%),default is 5%: ')
                i=int(i)
                if i<5 or i>25:
                    i=0.05
                else:
                    i=float(i/100)
            else:
                i=input('please enter the value(1-20), defalut is 1: ')
                i=int(i)
                if i<1 or i>20:
                    i=1                         
            print('please wait...')
            resultPath=os.path.join(path,'infrequent')
            curCollector=Collector()
            modelName='demo-model-exp4.txt'
            resultName='demo-result-exp4.txt'
            
            curCollector.dataCollector(trainPath)
            curCollector.infrequentProcess(i)
           
            curCollector.buildModel(resultPath,modelName)
            
            curCollector.doClassify(testPath,resultPath,resultName)
                    
            
                
        elif index==5:
            print('you choose delta change experiment')
            i=input('please choose the value of delta(0.1-1), default is 0.5: ')
            print('please wait...')
            if float(i)<0.1 or float(i)>1:
                i=0.5
            newDelta=float(i)
            
            
            resultPath=os.path.join(path,'delta')
            curCollector=Collector()
            curCollector.delta=newDelta
            modelName='demo-model-exp5.txt'
            resultName='demo-result-exp5.txt'
            self.doExpClassify(trainPath, testPath, resultPath, curCollector, modelName, resultName)
            
                
                
        else:
            print('you choose baseLine experiment')
            print('please wait...')
            resultPath=os.path.join(path,'baseLine')
            modelName='demo-model-base.txt'
            resultName='demo-result-base.txt'
            self.doExpClassify(trainPath, testPath, resultPath, collector, modelName, resultName)
            
            
        
    def doExpClassify(self,trainPath,testPath,resultPath,collector,modelName,resultName):
        
        collector.dataCollector(trainPath)
        
        
        collector.buildModel(resultPath,modelName)
        
        collector.doClassify(testPath,resultPath,resultName)
       

                
                
        
        