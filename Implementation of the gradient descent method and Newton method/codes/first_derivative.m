function [energy]= first_derivative(A)
energy_x=(1/(1-A(1)-A(2)))-(1/A(1));
energy_y=(1/(1-A(1)-A(2)))-(1/A(2));
energy=[energy_x;energy_y];
end