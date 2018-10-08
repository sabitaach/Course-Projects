function train_perceptron(eta,weights,DesiredClass,S,number_of_vectors)
epoch=0;
convergence_counter=0;
while(convergence_counter==0)
    error=0;
    
   %finding the number of misclassifications
    for t=1:number_of_vectors
        resultingClass=0; %zeros(1, number_of_vectors);%keeping track of the resulting class 
        each_input=[1 S{t}(1) S{t}(2)];
        input_times_weight=each_input*transpose(weights); 
        if(ge(input_times_weight,0))
            resultingClass=1;
        elseif(input_times_weight<0)
            resultingClass=0;
        end
        if(resultingClass~=DesiredClass(t))
            error=error+1;
        end
    end
    fprintf('\n %d errors in epoch %d\n', error, epoch)
    error_vector(epoch+1)=error;%used for generating the bar chart
    
    if(error==0)
        fprintf('\nCompleted in %d number of epoches\n',epoch)
        fprintf('\n ***These are the final weights***\n')
        weights
        convergence_counter=1;
        %generating bar chart
        x=0:1:epoch;
        y=error_vector;
        figure()
        bar(x,y)
        title(['Number of misclassifications when \eta=',num2str(eta)])
        xlabel('number of epoch ')
        ylabel('number of misclassifications')
    
    else
        %starting with one epoch    
        for z=1:number_of_vectors
            resultingClass=0;%keeping track of the resulting class 
            each_input=[1 S{z}(1) S{z}(2)];
            input_times_weight=each_input*transpose(weights);
            if(ge(input_times_weight,0))
                resultingClass=1;
            elseif(input_times_weight<0)
                resultingClass=0;
            end
            if(resultingClass~=DesiredClass(z))
                if(DesiredClass(z)==0)
                    weights=weights-eta*each_input;
                else
                    weights=weights+eta*each_input;
                end

             end
        end
        epoch=epoch+1;
    end
           
        
end
