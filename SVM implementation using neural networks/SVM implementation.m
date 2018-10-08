points=rand(100,2);
count_pos=0;
count_neg=0;
sigma=0.06;
for i=1:100
   v_1=(1/5)*sin(10*points(i,1))+0.3;
   v_2=(points(i,2)-0.8)^2+(points(i,1)-0.5)^2;
   if((points(i,2)<v_1)||(v_2<(0.15)^2))
       desired(i)=1;
       count_pos=count_pos+1;
       pos_point=[points(i,1),points(i,2)];
       pos_point_x(count_pos)=points(i,1);
       pos_point_y(count_pos)=points(i,2);
   else 
       desired(i)=-1;
       count_neg=count_neg+1;
       neg_point=[points(i,1),points(i,2)];
       neg_point_x(count_neg)=points(i,1);
       neg_point_y(count_neg)=points(i,2);
       
   end
end

 figure()
    hold on;
     
      scatter(pos_point_x,pos_point_y,'filled','k','s')
 scatter(neg_point_x,neg_point_y,'filled','b','d')
   %  title([' Misclassification rate with \eta=',num2str(eta)] )
      xlabel('x axis ')
   ylabel('y axis')
  % legend('test data')
   
   hold off;
   
   % this is the part where we start the optimization 
   
   f=-1*ones(100,1);
   prod_of_desired=transpose(desired)*desired;
 
  
   for l=1:100
       for k=1:100
           temp=norm(points(l,:)-points(k,:))^2;
           %sigma=50;
          from_kernel(l,k)=exp(-1*temp/sigma);
           % from_kernel(l,k)=(1+dot(points(l,:),points(k,:)))^9;% this is the polynomial kernel
           
       end
   end
   
   
  H=prod_of_desired.*(from_kernel);
  A=[];
  b=[];
   Aeq=desired;
   beq=0;
   lb=zeros(100,1);
   options = optimoptions('quadprog','Algorithm','interior-point-convex','Display','off');
   
  [alpha,fval,exitflag,output,lambda]=quadprog(H,f,A,b,Aeq,beq,lb,[],[],options);
   
   
   %now we are finding out the value of g(x)
   % for theta
     d=0; 
     for k=1:100
         if(alpha(k)~=0)
             d=d+1;
            req(d)=k;
            
         end
        
     end
     
     final_theta=0;
     for sz=1:d
        val_sup_vec(sz,:)=points(req(sz),:);
        label_sup_vec(sz)=desired(req(sz));
        %  theta=label_sup-
        later_portion=0;
        for k=1:100
           temp=norm(points(k,:)-val_sup_vec(sz,:))^2;
           %sigma=50;
          later_portion=later_portion+(desired(k)*alpha(k)*exp(-1*temp/sigma));
           % from_kernel(l,k)=(1+dot(points(l,:),points(k,:)))^9;% this is the polynomial kernel
           
        end
    
        theta(sz)=label_sup_vec(sz)-later_portion;
        final_theta=final_theta+theta(sz);
    %getting the indexes of all support vectors
    
     end
    final_theta=final_theta/d;
    
  id=1;  
   % for x1=0:0.001:1
    %    for x2=0:0.001:1
    y=1;y1=1;y2=1;
    for x1=0:0.001:1
         for x2=0:0.001:1
            g=0;
            for d=1:100
                if (alpha(d)~=0)
                    temp1=norm(points(d,:)-[x1 x2])^2;
                    g=g+(alpha(d)*desired(d)*exp(-1*temp1/sigma));
                end
            end
            g=g+final_theta;
            
            %if(abs(0-g)<=0.06)
            
            %if(round(g)==0)
           % if(round(g,2)==0.00)
           if(abs(0-g)<=0.001)
                ss1(y)=x1;
                ss2(y)=x2;
                y=y+1;
            end
            
            if(abs(1-g)<=0.002)
                ss11(y1)=x1;
                ss21(y1)=x2;
                y1=y1+1;
             end
            
           if(abs(-1-g)<=0.002)
                ss12(y)=x1;
                ss22(y)=x2;
                y2=y2+1;
            end
            
                
        end
    end
    
    hold on;
     
      scatter(ss1,ss2,5,'g','filled')
      scatter(ss11,ss21,5,'r','filled')
      scatter(ss12,ss22,5,'y','filled')
    hold off;
    
    
   
   
   
