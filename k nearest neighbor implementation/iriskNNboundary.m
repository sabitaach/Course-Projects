function iriskNNboundary(valueOfK)
D = csvread('iris.csv');

X_train = D(:, 1:2);%all rows of the first and second column
y_train = D(:, end); %all rows of the last column

% setup meshgrid
[x1, x2] = meshgrid(2:0.01:5, 0:0.01:3);
grid_size = size(x1);
X12 = [x1(:) x2(:)];%this is converting the info in the mesh to matrix format with x1 and x2

% compute 1NN decision 
n_X12 = size(X12, 1);%returns the number of rows
decision = zeros(n_X12, 1);%a matrix with n_X12 rows and 1 column of zeros
for i=1:n_X12 
    countOf1=0;countOf2=0;countOf3=0;
    point = X12(i, :);%these are each points in the mesh
    % compute euclidan distance from the point to all training data
    dist = pdist2(X_train, point);
    
    % sort the distance, get the index
    [~, idx_sorted] = sort(dist);%in [A,B], A  is the sorted result and B is the sorted index of the original data
   % fprintf('\nthe index of the sorted %f', idx_sorted);
    % find the class of the nearest neighbour
    for j=1:valueOfK
     prediction=y_train(idx_sorted(j));
     if prediction==1
         countOf1=countOf1+1;
     elseif prediction==2
             countOf2=countOf2+1;
     else
         countOf3=countOf3+1;
     end
    end
   % fprintf('\n the counts: count of 1: %d  countof2:%d  countof 3: %d',countOf1,countOf2,countOf3);
   % pred = y_train(idx_sorted(1));
   if ((countOf1>countOf2 &&countOf1>countOf3)||(countOf1>countOf3 && countOf1==countOf2)||(countOf1>countOf2 &&countOf1==countOf3)||(countOf1==countOf2 && countOf1==countOf3))
       decision(i)=1;
   elseif ((countOf2>countOf1 && countOf2>countOf3)||(countOf2>countOf1 &&countOf2==countOf3)||(countOf2>countOf3 &&countOf2==countOf1))
       decision(i)=2;
   else
       decision(i)=3;
   end
   % decision(i) = pred;
end

fileName=['For value of k=',num2str(valueOfK)];
figure('Name', fileName,'NumberTitle','off');
% plot decisions in the grid
decisionmap = reshape(decision, grid_size);
imagesc(2:0.01:5, 0:0.01:3, decisionmap);
set(gca,'ydir','normal');

% colormap for the classes
% class 1 = light red, 2 = light green, 3 = light blue
cmap = [1 0.8 0.8; 0.8 1 0.8; 0.8 0.8 1];
colormap(cmap);

% satter plot data
hold on;

scatter(X_train(y_train == 1, 1), X_train(y_train == 1, 2), 10, 'r');
scatter(X_train(y_train == 2, 1), X_train(y_train == 2, 2), 10, 'g');
scatter(X_train(y_train == 3, 1), X_train(y_train == 3, 2), 10, 'b');
hold off;