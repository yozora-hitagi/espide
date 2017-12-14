_espide_dump = function()
    local buf
    local j = 0
    if file.open("_espide_script_args0", "r") then
        print('--HexDump start')
        repeat
            buf = file.read(1024)
            if buf ~= nil then
                local n
                if #buf == 1024 then
                    n = (#buf / 16) * 16
                else
                    n = (#buf / 16 + 1) * 16
                end
                for i = 1, n do
                    j = j + 1
                    if (i - 1) % 16 == 0 then
                        uart.write(0, string.format('%08X  ', j - 1))
                    end
                    uart.write(0, i > #buf and '   ' or string.format('%02X ', buf:byte(i)))
                    if i % 8 == 0 then uart.write(0, ' ') end
                    if i % 16 == 0 then uart.write(0, buf:sub(i - 16 + 1, i):gsub('%c', '.'), '\n') end
                    if i % 128 == 0 then tmr.wdclr() end
                end
            end
        until (buf == nil)
        file.close()
        print("\n--HexDump done.")
    else
        print("\n--HexDump error: can't open file")
    end
end
_espide_dump()
_espide_dump = nil
