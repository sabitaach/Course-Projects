function train_test(n,eta,epsilon,epoch,W)

    trainlbl = fopen('train-labels.idx1-ubyte','r','b'); % first we have to open the binary file
    fseek(trainlbl,8,'bof');%jumping to the 8th offset because the values of labels only occur from 8th position
    trainingLabel=fread(trainlbl,n,'uchar');
    %creating a matrix that will store 1 only at the index of training label
    desired_label=zeros(10,n);
    for b=1:n
        desired_label(trainingLabel(b)+1,b)=1;
    end

    %now getting the contents from the image
    trainImg = fopen('train-images.idx3-ubyte','r','b'); % first we have to open the binary file
    flag=1;
    while(flag==1)
        errors(epoch)=0;
        %Starting with 3.1
        for s=1:n
         fseek(trainImg,16+(784*(s-1)),'bof');%the values for the picture starts from 16th position
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
           if (((errors(epoch-1))/n)>epsilon)
               flag=1;
           else
               flag=0;
           end
       if (n==60000 && (mod(epoch,52)==0))%plotting graphs after every 52 epochs when weights do not converge
            %plotting the epoch vs number of misclassifications 
            x_axis=0:1:epoch-2;
            y_axis=errors
            figure()
            bar(x_axis,y_axis)
            title(['For \eta=',num2str(eta),', n=',num2str(n), ', \epsilon=',num2str(epsilon)] )
            xlabel('number of epoch ')
            ylabel('number of misclassifications')
        end

    
    end

  %for cases when the weights converge, we just plot once
     %plotting the epoch vs number of misclassifications 
      x_axis=0:1:epoch-2;
      y_axis=errors
      figure()
      bar(x_axis,y_axis)
      title(['For \eta=',num2str(eta),', n=',num2str(n), ', \epsilon=',num2str(epsilon)] )
      xlabel('number of epoch ')
      ylabel('number of misclassifications')
    

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

end