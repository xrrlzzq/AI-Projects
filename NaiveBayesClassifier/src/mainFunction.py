from DataExperiment import DataExperiment
while True:
    exp=DataExperiment()
    print('welcome to use email filter')
    i=input('please enter the number to choose experiment,baseLine(1),stopWord(2),wordLength(3),infrequent(4),delta(5),default is baseLine')
    if i not in ['1','2','3','4','5']:
        i=1
    exp.doExperiment(int(i))
    j=input('do you want continue( default is not continue): continue(1), do not continue(2)')
    if not j=='1':
        break
    
        