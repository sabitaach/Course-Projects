x=1:50
x(2,:)=1
u_min=-1;
u_max=1;
for i=1:50
    u=u_min+rand(1)*(u_max-u_min);%generating random number between -1 and 1 in each loop
    y(i)=i+u;
    
end

%the minimizer of the error is w=y*pseudoinverse(x)
optimal_w=y*pinv(x)


 figure()
 x1=1:50
 hold on;
 y1=optimal_w(1)*x1+optimal_w(2);
 plot(x1,y1)
 scatter(x(1,:),y,15,'r','filled')
 xlabel('values of x ')
 ylabel('values of y')
 hold off;    