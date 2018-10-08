function [energy1]=second_derivative(A)

energy_x11=((1/(1-A(1)-A(2))^2)+(1/(A(1))^2))
energy_x12=1/(1-A(1)-A(2))^2
energy_y11=1/(1-A(1)-A(2))^2
energy_y12=((1/(1-A(1)-A(2))^2)+(1/(A(2))^2))
energy1=[energy_x11,energy_x12;energy_y11,energy_y12]
end