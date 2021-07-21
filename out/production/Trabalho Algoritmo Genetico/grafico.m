[group, id] = findgroups(GA.Geracao);
func = @(p, q, r, s) [mean(p), mean(q), mean(r)];
result = splitapply(func, GA.Melhor, GA.Media, GA.Pior, group);
plot(result(:,1))
hold on
plot(result(:,2))
plot(result(:,3))
hold off

[group, id] = findgroups(GA.Execucao);
func = @(p) [p(end)];
result = splitapply(func, GA.Melhor, group);
v = var(result)
s = std(result)