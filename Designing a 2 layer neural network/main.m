%generating a random number uniformly that falls in the range [-1/4,1/4]
w0_min=-1/4;
w0_max=1/4;
w0=w0_min+rand(1)*(w0_max-w0_min);

%generating a random number uniformly that falls in the range [-1,1]
w1_min=-1;
w1_max=1;
w1=w1_min+rand(1)*(w1_max-w1_min);

%generating a random number uniformly that falls in the range [-1,1]
w2_min=-1;
w2_max=1;
w2=w2_min+rand(1)*(w2_max-w2_min);

fprintf('these are the values: wo: %f, w1: %f,  w2:%f',w0,w1,w2)

number_of_vectors=1000;
%creating a collection of number_of_vectors vectors

for k = 1 : number_of_vectors
    S{k}=[w2_min+rand(1)*(w2_max-w2_min) w2_min+rand(1)*(w2_max-w2_min)];
end

%z=S{1}
%S{1}(1)

counter1=0;%keeps track of the numbers in S1
counter2=0;%keeps track of the numbers in S0
DesiredClass=zeros(1, number_of_vectors); %keeps track of the desired class
for l=1:number_of_vectors
    x_vector=[1 S{l}(1) S{l}(2)];
    weight_vector=[w0 w1 w2];
    result_of_mult=x_vector*transpose(weight_vector);
    if(ge(result_of_mult,0))
        counter1=counter1+1;
        S1{counter1}=[S{l}(1) S{l}(2)];%contains both (x1,x2)
        S1_x1(counter1)=S{l}(1);%only x1 --used for plotting
        S1_x2(counter1)=S{l}(2);% only x2--used for plotting
        DesiredClass(l)=1;
    elseif (result_of_mult<0)
        counter2=counter2+1;
        S0{counter2}=[S{l}(1) S{l}(2)];
        S0_x1(counter2)=S{l}(1);
        S0_x2(counter2)=S{l}(2);
        DesiredClass(l)=0;
    end
end


 


 hold on;
 scatter(S1_x1,S1_x2,15,'r','filled')
 scatter(S0_x1,S0_x2,'filled','b','d')
 refline(-w1/w2,-w0/w2)
 legend('S1','S0','Boundary')

% the below one also works--we can use either one of them for plotting the
% line
  % x1= linspace(-1,1); 
  %y1= (-w0/w2)+(-w1/w2)*x1;
  %plot(x1,y1)
 
 hold off;


  
% Starting for part j


w0_prime=w2_min+rand(1)*(w2_max-w2_min);
w1_prime=w2_min+rand(1)*(w2_max-w2_min);
w2_prime=w2_min+rand(1)*(w2_max-w2_min);
weights=[w0_prime w1_prime w2_prime];
 fprintf('\nthese are the initial weights: w0:%f  w1: %f w2: %f',w0_prime, w1_prime, w2_prime)

values_of_eta=[1,10,0.1];
for eta=values_of_eta(1:end)
   train_perceptron(eta,weights,DesiredClass,S,number_of_vectors);
    
end

 
