eta=1;
epsilon=0;
epoch=1;%starting with 1 as the value of epoch just because it does not allow to populate index 0, I take care of it while plotting graph

%for part d) 1) initializing W matrix with random numbers between 1 and -1
W=rand(10,784);

values_of_n=[50,1000,60000];% this is for f,g,h

for n=values_of_n(1:end)
  train_test(n,eta,epsilon,epoch,W);
  clear;
    
end

%for part i

n=60000; eta=1; epsilon=0.128;epoch=1;
for o=1:3
    W=rand(10,784);%passing different intial weights
    train_test(n,eta,epsilon,epoch,W);
end