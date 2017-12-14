_espide_download = function()
    file.open("_espide_script_args0", "r")
    local buf
    local i = 0
    local checksum
    repeat
        buf = file.read(1024)
        if buf ~= nil then
            i = i + 1
            checksum = 0
            for j = 1, string.len(buf) do
                checksum = checksum + (buf:byte(j) * 20) % 19
            end
            buf = '~~~' .. 'DATA-START~~~' .. buf .. '~~~' .. 'DATA-LENGTH~~~' .. string.len(buf) .. '~~~' .. 'DATA-N~~~' .. i .. '~~~' .. 'DATA-CRC~~~' .. checksum .. '~~~' .. 'DATA-END~~~'
            uart.write(0, buf)
        end
        tmr.wdclr()
    until (buf == nil)
    file.close()
    buf = '~~~DATA-TOTAL-START~~~' .. i .. '~~~DATA-TOTAL-END~~~'
    uart.write(0, buf)
end
_espide_download()
_espide_download = nil
