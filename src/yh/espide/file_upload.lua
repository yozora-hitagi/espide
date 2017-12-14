_espide_upload = function(n, l, ll)
    local cs = 0
    local i = 0
    print(">" .. " ")
    uart.on("data", l, function(b)
        i = i + 1
        file.open(" _espide_script_args0 ", 'a+')
        file.write(b)
        file.close()
        cs = 0
        for j = 1, l do
            cs = cs + (b:byte(j) * 20) % 19
        end
        uart.write(0, "~~~CRC-" .. "START~~~" .. cs .. "~~~CRC-" .. "END~~~")
        if i == n then
            uart.on("data")
        end
        if i == n - 1 and ll > 0 then
            _up(1, ll, ll)
        end
    end, 0)
end
file.remove("_espide_script_args0");
_espide_upload(_espide_script_args1, _espide_script_args2, _espide_script_args3)