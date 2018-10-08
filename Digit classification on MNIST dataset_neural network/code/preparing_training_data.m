
n=50;%this is the size of training data
%storing the training label in a matrix
trainlbl = fopen('train-labels.idx1-ubyte','r','b'); % first we have to open the binary file
fseek(trainlbl,8,'bof');%adding 7 because the values of labels only occur from 8th position
trainingLabel=fread(trainlbl,n,'uchar');
%creating a matrix that will store 1 only at the index of training label
desired_label=zeros(10,n);
for b=1:n
    desired_label(trainingLabel(b)+1,b)=1;
end

%now getting the contents from the image
trainImg = fopen('train-images.idx3-ubyte','r','b'); % first we have to open the binary file
%MagicNumber=fread(trainImg,1,'int32')
%nImages= fread(trainImg,1,'int32')% Read the number of images
%nRows= fread(trainImg,1,'int32')% Read the number of rows in each image
%nCols= fread(trainImg,1,'int32')% Read the number of columns in each image


eta=1;
epsilon=0.1;
 epoch=1;%adding 1 just because it does not allow to populate index 0
%d) 1) initializing W matrix with random numbers between 1 and -1

rand_min=-1; rand_max=1;
W=rand(10,784);
flag=1;
while(flag==1)
    errors(epoch)=0;
    %Starting with 3.1
    for s=1:n
     fseek(trainImg,16+(784*(s-1)),'bof');
     x= fread(trainImg,784,'uchar');% each image has 28*28 pixels in unsigned byte format   
     v=W*x; 

     desiredIndex=0; largest_value=v(1);
     for k=2:10
         if(v(k)>largest_value)
             largest_value=v(k);
             desiredIndex=k-1;
         end
     end

     if(desiredIndex~=trainingLabel(s))
        errors(epoch)=errors(epoch)+1;

     end
    end

    epoch=epoch+1
   
    for p=1:n %this is the loop to update the weights
        fseek(trainImg,16+(784*(p-1)),'bof');
        q= fread(trainImg,784,'uchar');% each image has 28*28 pixels in unsigned byte format   
        %applying step function in W
        W_times_x=W*q;
        for d=1:10
            if(W_times_x(d)>=0)
                    W_times_x(d)=1;
                else
                    W_times_x(d)=0;
            end
        end
       W=W+(eta*(desired_label(:,p)-W_times_x)*transpose(q));
    end
       if ((errors(epoch-1)>epsilon)&& epoch<302)
           flag=1;
       else
           %fprintf('the last value of error before trainig ends is: %d',errors(epoch-1))
           flag=0;
       end
  %  if (n==60000 && (mod(epoch,52)==0))%plotting graphs after every 100 epochs
        %plotting the epoch vs number of misclassifications 
   %{    
 x_axis=0:1:epoch-2;
        y_axis=errors
        figure()
        bar(x_axis,y_axis)
        title(['For \eta=',num2str(eta),', n=',num2str(n), ', \epsilon=',num2str(epsilon)] )
        xlabel('number of epoch ')
        ylabel('number of misclassifications')
    end

%}
end

if(n~=60000)%for other cases, we just plot once
    %plotting the epoch vs number of misclassifications 
        x_axis=0:1:epoch-2;
        y_axis=errors
        figure()
        bar(x_axis,y_axis)
        title(['For \eta=',num2str(eta),', n=',num2str(n), ', \epsilon=',num2str(epsilon)] )
        xlabel('number of epoch ')
        ylabel('number of misclassifications')
end







%begining of e) i.e the testing part



%storing the testing label in a matrix
testlbl = fopen('t10k-labels.idx1-ubyte','r','b'); % first we have to open the binary file
fseek(testlbl,8,'bof');%adding 7 because the values of labels only occur from 8th position
testingLabel=fread(testlbl,10000,'uchar');

%now getting the contents from the image
testImg = fopen('t10k-images.idx3-ubyte','r','b'); % first we have to open the binary file

error_test=0;

for h=1:10000
     fseek(testImg,16+(784*(h-1)),'bof');
     x_prime= fread(testImg,784,'uchar');% each image has 28*28 pixels in unsigned byte format   
     v_prime=W*x_prime; 

     desiredIndex_test=0;%keeping track of the index of largest value
     largest_value_test=v_prime(1);%keeps track of largest value
     for k=2:10
         if(v_prime(k)>largest_value_test)
             largest_value_test=v_prime(k);
             desiredIndex_test=k-1;
         end
     end

     if(desiredIndex_test~=testingLabel(h))
        error_test=error_test+1;

     end
end

fprintf('the final error while testing is : %d', error_test);
    



    
%{
fseek(trainImg,16,'bof');
img= fread(trainImg,28*28,'uchar');% each image has 28*28 pixels in unsigned byte format
img2=zeros(28,28);
for i=1:28
img2(i,:)=img((i-1)*28+1:i*28);
end
imshow(img2)
%}
%trying for the second image
%{
fseek(trainImg,16+28*28,'bof');
img3= fread(trainImg,28*28,'uchar');% each image has 28*28 pixels in unsigned byte format
img4=zeros(28,28);
figure
for i=1:28
img4(i,:)=img3((i-1)*28+1:i*28);
end
imshow(img4)

%}


%{
%this is the backup that I had obtained from the online file
fseek(trainImg,16,'bof');
img= fread(trainImg,28*28,'uchar');% each image has 28*28 pixels in unsigned byte format
img2=zeros(28,28);
for i=1:28
img2(i,:)=img((i-1)*28+1:i*28);
end
imshow(img2)


%}