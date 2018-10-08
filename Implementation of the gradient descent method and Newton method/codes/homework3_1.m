
w0=[0.09,0.04];
eta=0.01;

energy(1)=original_function(w0);
values_of_w(1,1)=w0(1);
values_of_w(1,2)=w0(2);
current_w=w0;
updated_w=[0,0];
i=2;
check=0;

while(check~=1)
  gradient=first_derivative(current_w)
  updated_w(1)=current_w(1)-eta*gradient(1);
  updated_w(2)=current_w(2)-eta*gradient(2);
  values_of_w(i,1)=updated_w(1);
  values_of_w(i,2)=updated_w(2);
  energy(i)=original_function(updated_w); 
 
   
   current_w=updated_w;
   
   if((values_of_w(i-1,1)==values_of_w(i,1))&&(values_of_w(i-1,2)==values_of_w(i,2)))
       check=1;
   end
  
   i=i+1;
end
    

x_axis=0:1:i-2;
y_axis=energy;
figure()
line(x_axis,y_axis)
title('Plot for energy vs number of iterations with gradient descent')
xlabel('iterations ')
ylabel('energy')
   
 %plotting the  individual points
 figure()
 hold on;
 line(values_of_w(:,1),values_of_w(:,2))
 title('Plot of trajectory for gradient descent')
 xlabel('values of x ')
 ylabel('values of y')
  hold off;
