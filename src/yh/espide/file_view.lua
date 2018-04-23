_espide_view = function()
    local _line
    if file.open("_espide_script_args0", "r") then
        print("--FileView start")
        repeat _line = file.readline()
            if (_line ~= nil) then
                e = string.sub(_line, #_line, -1)
                if (e == "\n" or e == "\r") then
                    print(string.sub(_line, 1, -2))
                else
                    print(_line)
                end
            end
        until _line == nil
        file.close()
        print("--FileView done.")
    else
        print("\r--FileView error: can't open file")
    end
end
_espide_view() _espide_view = nil
